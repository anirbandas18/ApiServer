package app.bean;

public class ClientRequest {

	private int connId;
	
	private int timeout;

	public int getConnId() {
		return connId;
	}

	public void setConnId(int connId) {
		this.connId = connId;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + connId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientRequest other = (ClientRequest) obj;
		if (connId != other.connId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ClientData [connId=" + connId + ", timeout=" + timeout + "]";
	}

}
