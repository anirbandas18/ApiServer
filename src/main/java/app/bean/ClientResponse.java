package app.bean;

public class ClientResponse {

	private String status;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "ClientStatus [status=" + status + "]";
	}

	public ClientResponse(String status) {
		super();
		this.status = status;
	}

	public ClientResponse() {
		super();
	}
	
}
