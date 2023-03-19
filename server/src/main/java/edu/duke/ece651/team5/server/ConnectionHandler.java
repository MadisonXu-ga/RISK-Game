package edu.duke.ece651.team5.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ConnectionHandler implements Runnable {
    private Socket clientSocket;
    private String color;

    private InputStream in;
    private OutputStream out;

    private PrintWriter printWriter;
    private ObjectOutputStream objectOutputStream;
    private BufferedReader bufferedReader;
    private ObjectInputStream objectInputStream;

    public ConnectionHandler(Socket clientSocket, String color) throws IOException {
        this.clientSocket = clientSocket;
        this.color = color;

        this.in = this.clientSocket.getInputStream();
        this.out = this.clientSocket.getOutputStream();

        this.bufferedReader = new BufferedReader(new InputStreamReader(in));
        this.printWriter = new PrintWriter(out, true);

        initObjectStream();
    }

    private void initObjectStream() throws IOException {
        this.objectOutputStream = new ObjectOutputStream(new BufferedOutputStream(out));
    }

    @Override
    public void run() {
        // inform player who she is (send) (hardcode?)
        // need to send map to player? (send)
        // assign territory groups to player (send) (hardcode?)
        // need to send units number to player? (send)

        try {
            sendObject("You are the " + this.color + " player!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // sendObject(riskMap);
        // sendObject(territoryGroup);
        // recvString();

        try {
            closeIOs();
        } catch (IOException e) {
            System.err.println("The stream is closed. Cannot write to the stream.");
        }
    }

    /*
     * Seems like some problems here, matbe discard later
     */
    public void sendString(String str) {
        printWriter.println(str);
    }

    public void sendObject(Object ob) throws IOException {
        this.objectOutputStream.writeObject(ob);
        this.objectOutputStream.flush();
    }

    public String recvString() throws IOException {
        return bufferedReader.readLine();
    }

    public Object recvObject() throws ClassNotFoundException, IOException {
        this.objectInputStream = new ObjectInputStream(in);
        Object result = this.objectInputStream.readObject();
        this.objectInputStream.close();
        return result;
    }

    public void closeIOs() throws IOException {
        printWriter.close();
        bufferedReader.close();
        objectOutputStream.close();
    }
}
