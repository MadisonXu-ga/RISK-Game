package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import edu.duke.ece651.team5.shared.PlayerConnection;

public class ChatServer {
    private ServerSocket chatServerSocket;
    private int port;
    private HashMap<Integer, ChatRoom> chatRooms;
    private ThreadPoolExecutor threadPool;

    public ChatServer(int port) throws IOException {
        this.port = port;
        this.chatServerSocket = new ServerSocket(port);

        // BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(32);
        // this.threadPool = new ThreadPoolExecutor(20, 20, 100, TimeUnit.SECONDS,
        // workQueue);
        chatServerSocket.setSoTimeout(1200000);

        this.chatRooms = new HashMap<>();
    }

    public void start() throws IOException, ClassNotFoundException {
        while (true) {
            Socket clientSocket = this.chatServerSocket.accept();
            PlayerConnection playerConnection = new PlayerConnection(clientSocket);
            int gameID = (int) playerConnection.readData();
            chatRooms.get(gameID).addClient(playerConnection);
        }
    }

    public void addChatRoom(int gameID, ChatRoom chatRoom){
        chatRooms.put(gameID, chatRoom);
    }
}
