package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.RISKMap;

public class InitializationHandlerTest {
    @Test
    void testSendRecvObject() throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket socket1 = new Socket("localhost", 8888);
        Socket cSocket = serverSocket.accept();
        ObjectOutputStream oo_server = new ObjectOutputStream(cSocket.getOutputStream());
        ObjectInputStream oi_client = new ObjectInputStream(socket1.getInputStream());
        ObjectOutputStream oo_client = new ObjectOutputStream(socket1.getOutputStream());
        ObjectInputStream oi_server = new ObjectInputStream(cSocket.getInputStream());
        ConnectionHandler c = new InitializationHandler(oo_server, oi_server, "Green", new RISKMap());

        c.sendObject("This is from server! Tesing object now!");
        assertEquals("This is from server! Tesing object now!", (String) oi_client.readObject());

        oo_client.writeObject("This is from client! Testing object now!");
        assertEquals("This is from client! Testing object now!", (String) c.recvObject());

        oo_client.close();
        oi_client.close();
        socket1.close();
        cSocket.close();
        serverSocket.close();
    }
}
