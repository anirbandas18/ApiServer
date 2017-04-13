package app.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import app.bean.ClientRequest;
import app.bean.ClientResponse;
import app.resources.HTTPKeys;
import app.service.Server;
import app.utility.HTTPHelper;

public class ClientController extends Server implements Runnable, HTTPKeys {

	private final static Logger LOGGER = Logger.getLogger(ClientController.class.getName());

	private static final long oneMilisecond = 1000;

	private Socket client;
	
	private String clientAddress;

	public ClientController(Socket client) {
		super();
		this.client = client;
		this.clientAddress = client.getRemoteSocketAddress().toString();
	}

	private Map<String, Object> delegateRequest(ClientController controller,Map<String, Object> request)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Map<String, Object> response = new TreeMap<>();
		String httpVersion = (String) request.get(HTTP_VERSION);
		response.put(HTTP_VERSION, httpVersion);
		String action = (String) request.get(ACTION);
		String uri = (String) request.get(URI);
		String methodName = action.toLowerCase();
		String uriTokens[] = uri.split(URI_SEPARATOR);
		String parsedUri = "";
		Object requestContent = request.get(MESSAGE_BODY);
		Object responseContent = null;
		for (String token : uriTokens) {
			if (token.length() != 0) {
				String lower = token.toLowerCase();
				String upper = token.toUpperCase();
				String modifiedToken = upper.charAt(0) + lower.substring(1);
				parsedUri = parsedUri + modifiedToken;
			}
		}
		methodName = methodName + METHOD_NAME_SEPARATOR + parsedUri;
		Class<?> controllerClass = controller.getClass();
		Method[] methods = controllerClass.getDeclaredMethods();
		boolean flag1 = false;
		boolean flag2 = false;
		int i = 0;
		for (; i < methods.length; i++) {
			String mn = methods[i].getName();
			if (mn.equalsIgnoreCase(methodName)) {
				flag1 = true;
				break;
			} else if (mn.toLowerCase().contains(parsedUri.toLowerCase())) {
				flag2 = true;
			}
		}
		if (flag1) {
			responseContent = requestContent != null ? methods[i].invoke(controller, requestContent)
					: methods[i].invoke(controller);
			response.put(STATUS_CODE, 200);
			response.put(REASON_PHRASE, "OK");
			response.put(MESSAGE_BODY, responseContent);
		} else if (flag2) {
			responseContent = new ClientResponse("Invalid action for the specified URI");
			response.put(STATUS_CODE, 405);
			response.put(REASON_PHRASE, "Method Not Allowed");
			response.put(MESSAGE_BODY, responseContent);
		} else {
			responseContent = new ClientResponse("Invalid resource");
			response.put(STATUS_CODE, 404);
			response.put(REASON_PHRASE, "Not Found");
			response.put(MESSAGE_BODY, responseContent);
		}
		return response;
	}
	
	private ClientResponse put_ApiKill(ClientRequest clientData) {
		ClientResponse clientStatus = new ClientResponse();
		String connId = String.valueOf(clientData.getConnId());
		boolean success = false;
		synchronized (threadPool) {
			List<Thread> list = new ArrayList<Thread>(threadPool.keySet());
			for (Thread t : list) {
				if (t.getName().equals(connId)) {
					t.interrupt();
					success = true;
					break;
				}
			}
		}
		clientStatus.setStatus(success ? "OK" : "Invalid conn Id : " + connId);
		return clientStatus;
	}

	private Map<String,String> get_ApiServerStatus() {
		Map<String,String> serverStatus = new TreeMap<>();
		for (Thread t : threadPool.keySet()) {
			if (t.isAlive()) {
				try {
					Integer connId = Integer.parseInt(t.getName());
					long startTime = threadPool.get(t);
					long now = System.currentTimeMillis();
					Integer timeLeft = (int) ((now - startTime) / oneMilisecond);
					serverStatus.put(connId.toString(), timeLeft.toString());
				} catch (NumberFormatException e) {
					
				}
			}
		}
		return serverStatus;
	}

	private ClientResponse get_ApiRequest(ClientRequest clientData) {
		ClientResponse clientStatus = new ClientResponse();
		try {
			String connId = String.valueOf(clientData.getConnId());
			ConcurrentLinkedQueue<Thread> keySet = new ConcurrentLinkedQueue<Thread>(threadPool.keySet());
			Thread item = keySet.stream().filter(t -> t.getName().equals(connId)).findAny().orElse(null);
			if (item == null) {
				Thread currentThread = Thread.currentThread();
				//System.err.println("Old name : " + currentThread.getName());
				currentThread.setName(connId);
				//System.err.println("New name : " + currentThread.getName());
				threadPool.put(currentThread, System.currentTimeMillis());
				Thread.sleep(clientData.getTimeout() * oneMilisecond);
				clientStatus.setStatus("OK");
				currentThread = Thread.currentThread();
			} else {
				System.err.println("Thread already executing with connId : " + connId);
			}
		} catch (InterruptedException e) {
			clientStatus.setStatus("Killed");
		}
		return clientStatus;
	}

	@Override
	public void run() {
		try {
			InputStream input = client.getInputStream();
			OutputStream output = client.getOutputStream();
			BufferedReader requestReader = new BufferedReader(new InputStreamReader(input));
			BufferedWriter responseWriter = new BufferedWriter(new OutputStreamWriter(output, CHARACTER_ENCODING));
			Map<String, Object> request = HTTPHelper.parseRequest(requestReader);
			System.out.println(request);
			Map<String, Object> responseEntity = delegateRequest(this, request);
			String response = HTTPHelper.composeResponse(responseEntity);
			System.out.println(response);
			responseWriter.write(response);
			responseWriter.flush();
			output.flush();
			client.close();
		} catch (IOException | IllegalAccessException | InvocationTargetException | SecurityException e) {
			LOGGER.log(Level.SEVERE, e.getMessage(), e);
		} finally {
			System.err.println(clientAddress + " disconnected at "+ HTTPHelper.createHTTPDateString());
		}
	}

}
