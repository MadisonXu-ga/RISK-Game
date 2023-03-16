package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.Test;

public class ConnectionHandlerTest {
    @Test
    void testSendRecvStringObject() throws IOException, ClassNotFoundException {
        ServerSocket serverSocket = new ServerSocket(8888);
        Socket socket1 = new Socket("localhost", 8888);
        Socket cSocket = serverSocket.accept();
        ConnectionHandler c = new ConnectionHandler(cSocket, "Green");

        BufferedReader bf_client = new BufferedReader(new InputStreamReader(socket1.getInputStream()));
        PrintWriter pw_client = new PrintWriter(socket1.getOutputStream(), true);

        c.sendObject("This is from server! Tesing object now!");
        ObjectInputStream oi_client = new ObjectInputStream(socket1.getInputStream());
        assertEquals("This is from server! Tesing object now!", (String) oi_client.readObject());

        pw_client.println("This is from client! Tesing string now!");
        assertEquals("This is from client! Tesing string now!", c.recvString());

        c.sendString("This is from server! Testing string now!");
        assertEquals("This is from server! Testing string now!", bf_client.readLine());

        ObjectOutputStream oo_client = new ObjectOutputStream(socket1.getOutputStream());
        oo_client.writeObject("This is from client! Testing object now!");
        assertEquals("This is from client! Testing object now!", (String) c.recvObject());

        oo_client.close();
        oo_client.close();
        bf_client.close();
        pw_client.close();
        socket1.close();
        cSocket.close();
        serverSocket.close();
    }
}
