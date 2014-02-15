package core;

import java.io.IOException;
import java.io.ObjectInputStream;

import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.io.CCNVersionedInputStream;
import org.ccnx.ccn.io.NoMatchingContentFoundException;
import org.ccnx.ccn.profiles.VersioningProfile;
import org.ccnx.ccn.protocol.ContentName;
import org.ccnx.ccn.protocol.Interest;

import ui.IMallUI;

public class ContentUpdate implements Runnable{

	private ContentName versionedName;
	
	private IMallUI ui;
	
	private CCNHandle handler;
	
	private ContentSync sync;
	
	public ContentUpdate(ContentName versionedName, ContentSync sync) {
		this.versionedName = versionedName;
		this.ui = sync.getUI();
		this.handler = sync.getHandler();
		this.sync = sync;
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
			
			MessageCollection newCollection = (MessageCollection) ois.readObject();
			
			sync.updateCollection(newCollection);
			ui.showLine(newCollection.getContentString());
			ois.close();
			
		} catch (NoMatchingContentFoundException e) {
						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
