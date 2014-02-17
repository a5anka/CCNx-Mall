package core;

import java.io.Serializable;

public class MallMessage implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4358706133916440176L;

	// Valid for 1 minute
	private static final long defaultValidity = 300000;
	
	private String title;
	
	private String message;
	
	private long expireTime;
	
	public MallMessage(String title, String message) {
		this.title = title;
		this.message = message;
		this.expireTime = System.currentTimeMillis() + defaultValidity;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	public String toString() {
		StringBuilder messageStr = new StringBuilder();
		messageStr.append(getTitle() + "\n");
		messageStr.append(new String(new char[getTitle().length()]).replace('\0', '='));
		messageStr.append("\n" + getMessage() + "\n");

		return messageStr.toString();
	}
	
	public boolean isExpired() {
		return (expireTime <= System.currentTimeMillis());
	}

}

	