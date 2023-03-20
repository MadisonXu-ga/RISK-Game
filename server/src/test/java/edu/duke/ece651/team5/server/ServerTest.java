package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.RISKMap;

public class ServerTest {
    private Socket socket1;
    private Socket socket2;
    private Socket socket3;

    private void createClient(Socket s, int port) {
        Thread clientThread = new Thread(() -> {
            try {
                ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
                ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
                String msg = (String) ois.readObject();
                System.out.println(msg);
                if (msg.equals("First")) {
                    oos.writeObject(3);
                    String msg2 = (String) ois.readObject();
                    System.out.println(msg2);
                }

                RISKMap r = (RISKMap) ois.readObject();
                assertNotNull(r);

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        clientThread.start();
    }

    @Test
    void testMultipleClients() throws SocketException, IOException, InterruptedException, ClassNotFoundException {
        int port = 9999;
        Server server = new Server(port);
        this.socket1 = new Socket("localhost", port);
        this.socket2 = new Socket("localhost", port);
        this.socket3 = new Socket("localhost", port);

        createClient(socket1, port);
        createClient(socket2, port);
        createClient(socket3, port);

        server.acceptClients();
        server.initGame();

        socket1.close();
        socket2.close();
        socket3.close();

        server.stop();
    }
}
