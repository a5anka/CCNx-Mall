package core;

import java.io.Serializable;
import java.util.ArrayList;

public class MessageCollection implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -9184036767214268514L;

	private ArrayList<MallMessage> store;
	
	public MessageCollection() {
		store = new ArrayList<MallMessage>();
	}
	
	public void addMessage(MallMessage newMessage) {
		store.add(newMessage);
	}
	
	public synchronized void addMessage(String title, String message) {
		MallMessage newMessage = new MallMessage(title, message);

		store.add(newMessage);
	}
	
	public void addMessage(String title, String message, Long validity) {
		MallMessage newMessage = new MallMessage(title, message, validity);

		store.add(newMessage);
	}
	
	public void mergeStore(MessageCollection otherCollection) {
		replaceStoreWith(otherCollection.store);
	}

	public String getContentString() {
		StringBuilder messageStr = new StringBuilder();
		
		for(MallMessage message: store) {
			messageStr.append(message.toString() + "\n");
		}
		
		return messageStr.toString();
	}
	
	public boolean removeExpired() {
		boolean stateChanged = false;
		
		ArrayList<MallMessage> tempStore = new ArrayList<MallMessage>();
		for(MallMessage message: store) {
			if (!message.isExpired()) {
				tempStore.add(message);
			} else {
				stateChanged = true;
			}
		}
		
		if (stateChanged) {
			replaceStoreWith(tempStore);
		}
		
		return stateChanged;
	}

	private synchronized void replaceStoreWith(ArrayList<MallMessage> tempStore) {
		store = tempStore;		
	}

	
}
