
import java.awt.Container;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ClientGUI {
    
    // Important Constants
    private final int CLIENT_GUI_WIDTH = 480;
    private final int CLIENT_GUI_HEIGHT = 600;
    private final int WIDTH_ACCOM = 8;
    private final int PORT = 31415;
    
    // Framework
    private JFrame clientFrame;
    private Container clientPane;
    private Insets clientInsets;
    
    // Components
    private JLabel port;
    private JTextArea chatArea;
    private JScrollPane chatAreaWrapper;
    private JTextArea chatSubmit;
    private JScrollPane chatSubmitWrapper;
    private JButton submitButton;
    
    // Component constants
    private final int PORT_X = 10; 
    private final int PORT_Y = 10;
    private final int CHAT_AREA_WIDTH = 400;
    private final int CHAT_AREA_HEIGHT = 400;
    private final int CHAT_SUBMIT_WIDTH = 400;
    private final int CHAT_SUBMIT_HEIGHT = 50;
    
    // Misc
    private String chatMessage;
    private boolean updateReady = false;
    
    // Constructor
    public ClientGUI() {
        
    }
    
    public void initGUI() {
        try {
            // Fancy
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        }
        
        // Init frame
        clientFrame = new JFrame("Client");
        clientFrame.setSize(CLIENT_GUI_WIDTH, CLIENT_GUI_HEIGHT);
        clientFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Containers
        clientPane = clientFrame.getContentPane();
        clientPane.setLayout(null);
        
        // Insets
        clientInsets = clientFrame.getInsets();
        
        // Other components
        initComponents();
        
        // Finalize
        clientFrame.setVisible(true);
    }
    
    private void initComponents() {
        // Port label
        port = new JLabel("Port: " + Integer.toString(PORT));
        port.setBounds((clientInsets.left + CLIENT_GUI_WIDTH) / 2 - port.getPreferredSize().width / 2 - WIDTH_ACCOM, clientInsets.top + 10, port.getPreferredSize().width, port.getPreferredSize().height);
        clientPane.add(port);
        
        // Message chat area
        chatArea = new JTextArea();
        chatAreaWrapper = new JScrollPane(chatArea);
        chatArea.setEditable(false);
        chatArea.setLineWrap(true);
        chatAreaWrapper.setWheelScrollingEnabled(true);
        chatAreaWrapper.setBounds((clientInsets.left + CLIENT_GUI_WIDTH) / 2 - CHAT_AREA_WIDTH / 2 - WIDTH_ACCOM, clientInsets.top + 50, CHAT_AREA_WIDTH, CHAT_AREA_HEIGHT);
        clientPane.add(chatAreaWrapper);
        
        // Text submit area
        chatSubmit = new JTextArea();
        chatSubmitWrapper = new JScrollPane(chatSubmit);
        chatSubmit.setEditable(true);
        chatSubmit.setLineWrap(true);
        chatSubmitWrapper.setWheelScrollingEnabled(true);
        chatSubmitWrapper.setBounds((clientInsets.left + CLIENT_GUI_WIDTH) / 2 - CHAT_SUBMIT_WIDTH / 2 - WIDTH_ACCOM, clientInsets.top + 460, CHAT_SUBMIT_WIDTH, CHAT_SUBMIT_HEIGHT);
        clientPane.add(chatSubmitWrapper);
        
        // Buttons
        submitButton = new JButton("Send");
        submitButton.setBounds(clientInsets.left + 375, clientInsets.top + 520, submitButton.getPreferredSize().width, submitButton.getPreferredSize().height);
        submitButton.addActionListener(new SubmitListener());
        clientPane.add(submitButton);
    }
    
    private class SubmitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            chatMessage = chatSubmit.getText().trim();
            updateChat("[Client@" + Client.client.getClientSocket().getLocalPort() + "] " + chatMessage);
            chatSubmit.setText("");
            updateReady = true;
            //throw new UnsupportedOperationException("not supported");
        }
        
    }
    
    public void updateChat(String newText) {
        chatArea.append(newText + "\n");
        //chatArea.setText(newText);
    }
    
    public boolean getUpdateReady() {
        return updateReady;
    }
    
    public void setUpdateReady(boolean state) {
        updateReady = state;
    }
    
    public String getChatMessage() {
        return chatMessage;
    }
}
