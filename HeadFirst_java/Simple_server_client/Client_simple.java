package Simple_server_client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Client_simple {
    private JTextArea incoming;
    private JTextField outgoing;
    private BufferedReader reader;
    private PrintWriter writer;
    private Socket socket;
    public static void main(String[] args) {
        Client_simple client = new Client_simple();
        client.go();
    }
    
    private void go(){
        JFrame myFrame = new JFrame("Simple Client");
        JPanel mainPanel = new JPanel();

        incoming = new JTextArea(15, 50);
        incoming.setLineWrap(true);
        incoming.setWrapStyleWord(true);
        incoming.setEditable(false);

        JScrollPane incomePane = new JScrollPane(incoming);
        incomePane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        incomePane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        outgoing = new JTextField(20);
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(new SendButtonListener());

        mainPanel.add(incomePane);
        mainPanel.add(outgoing);
        mainPanel.add(sendButton);
        setupWorking();

        Thread thread = new Thread(new ThreadJob());
        thread.start();

        myFrame.getContentPane().add(BorderLayout.CENTER, mainPanel);
        myFrame.setSize(400,500);
        myFrame.setVisible(true);
        myFrame.setLocation(210, 100);
        // myFrame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private class SendButtonListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            try {
                // (BufferedWriter) writer.write(outgoing.getText() + "\n");
                writer.println(outgoing.getText());
                writer.flush();
            } catch (Exception err) {
                err.printStackTrace();
            }
            outgoing.setText("");
            outgoing.requestFocus();
        }
    }

    private void setupWorking(){
        try {
            socket = new Socket("127.0.0.1", 5000);
            InputStreamReader inputStreamReader = new InputStreamReader(socket.getInputStream());
            reader = new BufferedReader(inputStreamReader);

            // C2: OutputStreamWriter streamWriter = new OutputStreamWriter(socket.getOutputStream)
            // (BufferedWriter) writer = new BufferedWriter(streamWriter);
            writer = new PrintWriter(socket.getOutputStream());
            System.out.println("network established");
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
        }
    }

    private class ThreadJob implements Runnable{
        public void run(){
            String pen;
            try {
                while((pen = reader.readLine()) != null){
                    System.out.println("read: " + pen);
                    incoming.append(pen + "\n");
                }
            } catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
        }
    }
}
