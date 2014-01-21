package ui.gui;

import javax.swing.JFrame;

import org.ccnx.ccn.protocol.ContentName;
import org.ccnx.ccn.protocol.MalformedContentNameStringException;

import ui.IMallUI;
import core.ContentSync;

public class MallGUI implements Runnable, IMallUI{
	
	private JFrame frame;
	private ContentPane mainPane;
	private ContentSync syncClient;
	
	public MallGUI() {
		ContentName topology;
		ContentName namespace;
		
		try {
			topology = ContentName.fromNative("/mall");
			namespace = ContentName.fromNative("/floor1/posts");
			syncClient = new ContentSync(topology, namespace,this);
			
			//Create and set up the window.
	        frame = new JFrame("Mall App");
	        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        
	        mainPane = new ContentPane();
	        frame.setContentPane(mainPane.createContentPane(syncClient));
			
		} catch (MalformedContentNameStringException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }

	@Override
	public void start() {
		javax.swing.SwingUtilities.invokeLater(this);
		this.syncClient.start();
	}

	@Override
	public void showLine(String line) {
		mainPane.showLine(line);
	}

	@Override
	public void run() {
		this.createAndShowGUI();
	}

	private void createAndShowGUI() {
		//Display the window.
        frame.pack();
        frame.setVisible(true);
	}


}
