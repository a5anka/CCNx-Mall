package core;

import org.ccnx.ccn.CCNSyncHandler;
import org.ccnx.ccn.io.content.ConfigSlice;
import org.ccnx.ccn.protocol.ContentName;

public class SyncHandler implements CCNSyncHandler  {

	private ContentSync sync;
	
	public SyncHandler(ContentSync contentSync) {
		this.sync = contentSync;
	}

	@Override
	public void handleContentName(ConfigSlice syncSlice, ContentName syncedContent) {
		(new Thread(new ContentUpdate(syncedContent, sync))).start();
	}

}
