package core;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.io.CCNVersionedInputStream;
import org.ccnx.ccn.profiles.VersioningProfile;
import org.ccnx.ccn.protocol.ContentName;
import org.ccnx.ccn.protocol.Interest;

import ui.IMallUI;

public class ContentUpdate implements Runnable{

	private ContentName versionedName;
	
	private IMallUI ui;
	
	private CCNHandle handler;
	
	public ContentUpdate(ContentName versionedName, IMallUI ui, CCNHandle handler) {
		this.versionedName = versionedName;
		this.ui = ui;
		this.handler = handler;
	}


	@Override
	public void run() {

		/** De-bounce */
//		try {
//			Thread.sleep(250);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
//		
		/** Create Input Stream	 */
		Interest interest = VersioningProfile.latestVersionInterest(versionedName, null, null);
		try {
			CCNVersionedInputStream inputStream = new CCNVersionedInputStream(interest.name(), handler);

			ObjectInputStream ois = new ObjectInputStream(inputStream);
			
			MallMessage newMessage = (MallMessage) ois.readObject();
			
			StringBuilder messageStr = new StringBuilder();
			messageStr.append(newMessage.getTitle() + "\n");
			messageStr.append(new String(new char[newMessage.getTitle().length()]).replace('\0', '='));
			messageStr.append("\n" + newMessage.getMessage() + "\n");

			ui.showLine(messageStr.toString());

			ois.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
