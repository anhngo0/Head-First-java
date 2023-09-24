package Simple_server_client;

import java.io.*;
import java.net.*;
import java.util.*;

public class Server_simple {
    ArrayList<PrintWriter> clienOutputStreams;

    public class ClientHandler implements Runnable{
        BufferedReader reader;
        Socket socket;

        public ClientHandler(Socket clientSocket){
            try {
                socket = clientSocket;
                InputStreamReader inputStream = new InputStreamReader(socket.getInputStream());
                reader = new BufferedReader(inputStream);    
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public void run(){
            String message;
            try {
                while((message = reader.readLine()) != null){
                    System.out.println("read: "+message);
                    tellEveryone(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public static void main(String[] args) {
        Server_simple simpleServer = new Server_simple();
        simpleServer.go();
    }

    public void go(){
        clienOutputStreams = new ArrayList<PrintWriter>();
        try {
            ServerSocket serverSocket = new ServerSocket(5000);
            while(true){
                Socket socket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                clienOutputStreams.add(writer);
                Thread thread = new Thread(new ClientHandler(socket));
                thread.start();
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }
    public void tellEveryone(String message){
        Iterator it = clienOutputStreams.iterator();
        while(it.hasNext()){
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
    }
}
