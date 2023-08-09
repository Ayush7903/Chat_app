import java.net.*;
import java.io.*;

public class Client {
    Socket socket;
    BufferedReader data_Read;
    PrintWriter data_Write;
    boolean keep_doing;

    public Client() {
        try {
            System.out.println("Sending requuest to server");
            socket = new Socket("127.0.0.1", 1111);
            System.out.println("connection built");
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
                        System.out.println("Server terminated chat");
                        socket.close();
                        break;
                    } else {
                        System.out.println("Server : " + msg);
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
            try {

                while (true && !socket.isClosed()) {
                    // System.out.println("writer started");

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
        System.out.println("Client is going on");
        new Client();
    }
}
