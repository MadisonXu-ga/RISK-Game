package edu.duke.ece651.team5.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class PlayerChatConnection {
    private final Socket playerSocket;

    private final BufferedReader inputChat;
    private final PrintWriter outputChat;

    public PlayerChatConnection(Socket playerSocket) throws IOException {
        this.playerSocket = playerSocket;
        outputChat = new PrintWriter(
                new OutputStreamWriter(this.playerSocket.getOutputStream(), StandardCharsets.UTF_8),
                true);
        inputChat = new BufferedReader(
                new InputStreamReader(this.playerSocket.getInputStream(), StandardCharsets.UTF_8));
    }

    /**
     * read string
     * 
     * @return
     * @throws IOException
     */
    public String readString() throws IOException {
        return inputChat.readLine();
    }

    /**
     * write string
     * 
     * @param message
     */
    public void writeString(String message) {
        outputChat.println(message);
    }

    public Socket getSocket(){
        return playerSocket;
    }

    public void close() throws IOException {
        outputChat.close();
        inputChat.close();
        playerSocket.close();
    }
}
