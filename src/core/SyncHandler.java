package core;

import org.ccnx.ccn.CCNSyncHandler;
import org.ccnx.ccn.io.content.ConfigSlice;
import org.ccnx.ccn.profiles.VersionMissingException;
import org.ccnx.ccn.profiles.versioning.VersionNumber;
import org.ccnx.ccn.protocol.ContentName;

public class SyncHandler implements CCNSyncHandler  {

	private ContentSync sync;
	
	private VersionNumber latestVersion;
	
	public SyncHandler(ContentSync contentSync) {
		this.sync = contentSync;
		latestVersion = new VersionNumber(0);
	}

	@Override
	public void handleContentName(ConfigSlice syncSlice, ContentName syncedContent) {
		try {
			VersionNumber currentVersion = new VersionNumber(syncedContent);
			
			if(latestVersion.before(currentVersion)) {
				System.out.println("Latest: " + latestVersion.toString());
				System.out.println("Current: " + currentVersion.toString());
				latestVersion = currentVersion;
				(new Thread(new ContentUpdate(syncedContent, sync))).start();
			}
			
		} catch (VersionMissingException e) {
			e.printStackTrace();
		}
	}

}
