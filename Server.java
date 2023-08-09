import java.net.*;
import java.io.*;

class Server {

    // Constructor
    ServerSocket server;
    Socket socket;
    BufferedReader data_Read;
    PrintWriter data_Write;
    boolean keep_doing;

    public Server() {
        try {
            server = new ServerSocket(1111);
            System.out.println("Server is ready to accept connection");
            System.out.println("waiting to accept connection");
            socket = server.accept();
            data_Read = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            data_Write = new PrintWriter(socket.getOutputStream());
            startReading();
            startWriting();
            keep_doing = true;
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void startReading() {
        // thread for reading data
        Runnable r1 = () -> {
            System.out.println("reader started");
            try {
                while (true && !socket.isClosed()) {

                    String msg = data_Read.readLine();
                    if (msg.equals("exit")) {
                        keep_doing = false;
                        socket.close();
                        System.out.println("Client terminated chat");
                        break;
                    } else {
                        System.out.println("Client : " + msg);
                    }
                }

            } catch (Exception e) {
                System.out.println("Connection closed");
            }
        };
        new Thread(r1).start();
    }

    public void startWriting() {
        // thread for taking data from user amd send to client
        Runnable r2 = () -> {
            // System.out.println("writer started");
            try {
                while (true && !socket.isClosed()) {

                    if (!keep_doing) {
                        socket.close();
                        break;
                    }
                    BufferedReader new_reader = new BufferedReader(new InputStreamReader(System.in));
                    String content = new_reader.readLine();
                    data_Write.println(content);
                    data_Write.flush();
                }
            } catch (Exception e) {
                System.out.println("Connection closed");
            }
        };
        new Thread(r2).start();
    }

    public static void main(String args[]) {
        System.out.println("Server is going ");
        new Server();
    }
}