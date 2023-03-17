package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.SocketException;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.RISKMap;

public class ServerTest {
    @Disabled
    @Test
    void testSendMapToOneClient() throws IOException, ClassNotFoundException {
        Server server = new Server(6666);

        // connect
        Socket socket = new Socket("localhost", 6666);
        server.acceptClients(1);
        // send
        server.sendMapToOneClient(socket);
        // receive
        InputStream inputStream = socket.getInputStream();
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);

        /*
         * need more info to check
         * !! seems like that out objects need to implement Serializable (the easiest
         * way)
         * or turn into json or other formats (which need third-party serialization
         * library) !!
         */

        RISKMap map = (RISKMap) objectInputStream.readObject();
        assertNotNull(map);

        socket.close();
        server.stop();
    }

    @Test
    void testMultipleClients() throws SocketException, IOException, InterruptedException, ClassNotFoundException {
        Server server = new Server(7777);
        Socket socket1 = new Socket("localhost", 7777);
        Socket socket2 = new Socket("localhost", 7777);
        Socket socket3 = new Socket("localhost", 7777);

        server.acceptClients(3);
        server.initGame();

        BufferedReader bufferedReader1 = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        BufferedReader bufferedReader2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
        BufferedReader bufferedReader3 = new BufferedReader(new InputStreamReader(socket3.getInputStream()));

        // ObjectInputStream o1 = new ObjectInputStream(socket1.getInputStream());
        // ObjectInputStream o2 = new ObjectInputStream(socket2.getInputStream());
        // ObjectInputStream o3 = new ObjectInputStream(socket3.getInputStream());

        assertEquals("You are the Green player!", bufferedReader1.readLine());
        assertEquals("You are the Blue player!", bufferedReader2.readLine());
        assertEquals("You are the Red player!", bufferedReader3.readLine());

        // assertEquals("You are the Green player!", (String) o1.readObject());
        // assertEquals("You are the Blue player!", (String) o2.readObject());
        // assertEquals("You are the Red player!", (String) o3.readObject());

        socket1.close();
        socket2.close();
        socket3.close();
        server.stop();
    }
}
