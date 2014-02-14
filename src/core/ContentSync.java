package core;

import java.io.IOException;
import java.io.ObjectOutputStream;

import org.ccnx.ccn.CCNHandle;
import org.ccnx.ccn.CCNSync;
import org.ccnx.ccn.config.ConfigurationException;
import org.ccnx.ccn.io.RepositoryVersionedOutputStream;
import org.ccnx.ccn.io.content.ConfigSlice;
import org.ccnx.ccn.profiles.VersioningProfile;
import org.ccnx.ccn.protocol.ContentName;
import org.ccnx.ccn.protocol.MalformedContentNameStringException;

import ui.IMallUI;

public class ContentSync {
	
	private CCNSync ccnSyncClient;
	
	private ConfigSlice slice;
	
	private CCNHandle handler;

	private ContentName namespace;

	private ContentName topology;
	
	private IMallUI ui;

	private SyncHandler syncHandler;
	
	public ContentSync(ContentName topology, ContentName namespace, IMallUI ui) {
		
		this.namespace = namespace;
		this.topology = topology;
		this.ui = ui;
		
		
		try {	
			handler = CCNHandle.open();
		
			ccnSyncClient = new CCNSync();
			
			
			//Thread.sleep(5000);

		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
		}
	}
	
	public void start()
	{
		try {
			syncHandler = new SyncHandler(ui, handler);
			slice = ccnSyncClient.startSync(handler, topology, namespace, syncHandler);
		} catch (ConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String title, String message)
	{
		MallMessage newMessage = new MallMessage(title, message);
		
		ContentName messagePrefix;
		try {
			messagePrefix = this.namespace.append(title);
			ContentName versionedContentName = VersioningProfile.updateVersion(messagePrefix);
			
			//RepositoryOutputStream repout = new RepositoryOutputStream(versionedContentName, handler);
			RepositoryVersionedOutputStream repout = new RepositoryVersionedOutputStream(versionedContentName, handler);
			
			ObjectOutputStream oos = new ObjectOutputStream(repout);
			oos.writeObject(newMessage);
			oos.flush();
			repout.flush();
			oos.close();
		} catch (MalformedContentNameStringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	public void changeLocation(String newLocation) throws IOException, MalformedContentNameStringException {
		ccnSyncClient.stopSync(syncHandler, slice);
		this.namespace = ContentName.fromNative("/posts/" + newLocation);
		start();
	}

}
