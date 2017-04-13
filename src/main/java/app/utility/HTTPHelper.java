package app.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import org.apache.commons.beanutils.BeanUtils;

import com.google.gson.Gson;

import app.bean.ClientRequest;
import app.resources.HTTPKeys;

public class HTTPHelper implements HTTPKeys {


	/*
	 * Parse request line to get HTTP action, host and URI and request body
	 * according to HTTP action
	 */
	public synchronized static Map<String, Object> parseRequest(BufferedReader br)
			throws IOException, IllegalAccessException, InvocationTargetException {
		Map<String, Object> request = new TreeMap<>();
		String line = br.readLine();
		String requestLineTokens[] = line.split(SP);
		request.put(ACTION, requestLineTokens[0]);
		request.put(URI, requestLineTokens[1]);
		request.put(HTTP_VERSION, requestLineTokens[2]);
		line = br.readLine();
		String tokens[] = line.split(FIELD_SEPARATOR_REGEX);
		if(tokens.length > 2) {
			tokens[1] = tokens[1] + FIELD_SEPARATOR_REGEX + tokens[2];
		}
		request.put(tokens[0], tokens[1]);
		Object requestContent = null;
		String action = (String) request.get(ACTION);
		if (action.equalsIgnoreCase(GET)) {
			String uri = (String) request.get(URI);
			Map<String, String> parameters = new LinkedHashMap<>();
			int parameterListStartPosition = uri.indexOf(PARAMETER_LIST_START);
			if(parameterListStartPosition != -1) {
				String parameterList = uri.substring(parameterListStartPosition + 1);
				uri = uri.substring(0, parameterListStartPosition);
				request.put(URI, uri);
				for (String parameter : parameterList.split(PARAMETER_SEPARATOR)) {
					String parameterTokens[] = parameter.split(PARAMETER_VALUE_SEPARATOR);
					parameters.put(parameterTokens[0], parameterTokens[1]);
				}
				requestContent = new ClientRequest();
				BeanUtils.populate(requestContent, parameters);
			}
		} else {
			while ((line = br.readLine()).length() != 0) {
				tokens = line.split(FIELD_SEPARATOR_REGEX);
				switch (tokens[0]) {
				case CONTENT_LENGTH:
				case CONTENT_TYPE:
					request.put(tokens[0], tokens[1]);
					break;
				}
			}
			String contentLengthValue = (String) request.get(CONTENT_LENGTH);
			if (contentLengthValue != null) {
				int contentLength = Integer.parseInt(contentLengthValue);
				char content[] = new char[contentLength];
				br.read(content, 0, contentLength);
				String messageBody = new String(content, 0, contentLength);
				Gson gson = new Gson();
				requestContent = gson.fromJson(messageBody, ClientRequest.class);
			}
		}
		request.put(MESSAGE_BODY, requestContent);
		return request;
	}

	public static synchronized String createHTTPDateString() {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT, Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));
		String now = sdf.format(new Date());
		return now;
	}

	public static synchronized String composeResponse(Map<String,Object> responseEntity) {
		int code = (int) responseEntity.get(STATUS_CODE);
		Object description = responseEntity.get(REASON_PHRASE);
		Object responseContent = responseEntity.get(MESSAGE_BODY);
		String httpVersion = (String) responseEntity.get(HTTP_VERSION);
		Gson gson = new Gson();
		String responseBody = gson.toJson(responseContent);
		int contentLength = responseBody.length();
		String statusLine = httpVersion + SP + code + SP + description;
		String now = createHTTPDateString();
		StringBuffer responseHeaders = new StringBuffer();
		responseHeaders.append(DATE + FIELD_SEPARATOR + now + CR + LF);
		responseHeaders.append(CONTENT_TYPE + FIELD_SEPARATOR + CONTENT_TYPE_JSON + CR + LF);
		responseHeaders.append(CONTENT_LENGTH + FIELD_SEPARATOR + contentLength + CR + LF);
		StringBuffer response = new StringBuffer();
		response.append(statusLine);
		response.append(CR + LF);
		response.append(responseHeaders);
		response.append(CR + LF);
		response.append(responseBody);
		response.append(CR + LF);
		return response.toString();
	}


}
