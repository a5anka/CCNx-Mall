package core;

import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.CCNSyncHandler;
import org.ccnx.ccn.io.content.ConfigSlice;
import org.ccnx.ccn.protocol.ContentName;

import ui.IMallUI;

public class SyncHandler implements CCNSyncHandler  {

	private IMallUI ui;
	private CCNHandle handler;
	
	public SyncHandler(IMallUI ui, CCNHandle handler) {
		this.ui = ui;
	}

	@Override
	public void handleContentName(ConfigSlice syncSlice, ContentName syncedContent) {
		(new Thread(new ContentUpdate(syncedContent, ui, handler))).start();
	}

}
