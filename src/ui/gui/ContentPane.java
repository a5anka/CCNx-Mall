package ui.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.ccnx.ccn.protocol.MalformedContentNameStringException;

import core.ContentSync;

public class ContentPane {
	private JTextArea content;
	
	private JTextField titleText; 

	private JTextField messageText; 
	
	private JTextField locationText; 
	
	private ContentSync sync;

	private JComboBox validityList;

	private Map<String, Long> validityStrings;
	
	public JPanel createContentPane (ContentSync syncClient){
		this.sync = syncClient;
		JPanel contentPane = new JPanel();
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.PAGE_AXIS));
        
        JLabel locationLabel = new JLabel("Current Location");
        contentPane.add(locationLabel);
        
        locationText= new JTextField("floor1");
        contentPane.add(locationText);
        
        JButton updateButton = createUpdateButton();
        contentPane.add(updateButton);
        
        contentPane.add(Box.createRigidArea(new Dimension(0,10)));
        
        JLabel label = new JLabel("Content for location");
        contentPane.add(label);
        
        content = new JTextArea();
        content.setEditable(false);
        content.setLineWrap(true);
        content.setWrapStyleWord(true);
        JScrollPane contentScroller = new JScrollPane(content);
        contentScroller.setPreferredSize(new Dimension(300, 450));
        contentScroller.setAlignmentX(0);
        contentPane.add(Box.createRigidArea(new Dimension(0,5)));
        contentPane.add(contentScroller);
        
        JLabel titleLabel = new JLabel("Title");
        contentPane.add(titleLabel);
        
        titleText= new JTextField();
        titleText.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(titleText);
        
        JLabel messageLabel = new JLabel("Message");
        contentPane.add(messageLabel);
        
        messageText= new JTextField();
        messageText.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(messageText);
        
        JLabel validityLabel = new JLabel("Validity");
        contentPane.add(validityLabel);
        
        validityStrings = new HashMap<String, Long>();
        validityStrings.put("1 min", (long) 60000);
        validityStrings.put("5 min", (long) 300000);
        validityStrings.put("1 hour", (long) 3600000);
        validityStrings.put("6 hour", (long) 21600000);
        validityStrings.put("1 day", (long) 86400000);
        
        // Separate list needed for correct ordering of items in combobox
        String[] timeStrings = {"1 min", "5 min", "1 hour", "6 hour", "1 day"};
        
        validityList = new JComboBox(timeStrings);
        validityList.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentPane.add(validityList);
        		
        JPanel buttonPane = createHorizontalPane();
        //buttonPane.add(Box.createHorizontalGlue());
        JButton sendButton = createSendButton();
        buttonPane.add(sendButton);
        
        contentPane.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
        
        JPanel totalGUI = new JPanel();
        totalGUI.setLayout(new BorderLayout());
        totalGUI.add(contentPane,BorderLayout.CENTER);
        totalGUI.add(buttonPane, BorderLayout.PAGE_END);
        
		return totalGUI;
	}
	
	private JButton createSendButton() {
		JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new ActionListener() {
			
			@Override
			
			public void actionPerformed(ActionEvent arg0) {
				sync.sendMessage(titleText.getText(), 
						messageText.getText(), 
						validityStrings.get(validityList.getSelectedItem()));
				titleText.setText("");
				messageText.setText("");
			}
		});
        
        return sendButton;
	}

	private JButton createUpdateButton() {
		JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if	(!locationText.getText().equals("")) {
					try {
						clearContent();
						sync.changeLocation(locationText.getText());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (MalformedContentNameStringException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}	
		});
        
        return updateButton;
	}
	
	private JPanel createHorizontalPane () {
		JPanel tPane = new JPanel();
        tPane.setLayout(new BoxLayout(tPane, BoxLayout.LINE_AXIS));
        tPane.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
        
        return tPane;
	}
	
	public void showLine(String line) {
		content.append(line + "\n");
	}
	
	public void clearContent() {
		content.setText("");
	}

}
