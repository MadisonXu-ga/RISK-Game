package edu.duke.ece651.team5.server;

import java.util.ArrayList;

import edu.duke.ece651.team5.shared.PlayerConnection;

public class ChatRoom {
    private int gameID;
    private int playerNum;
    private ArrayList<PlayerConnection> playerConnections;


    public ChatRoom(int gameID, int playerNum) {
        this.gameID = gameID;
        this.playerNum = playerNum;
    }

    public void addClient(PlayerConnection playerConnection){
        playerConnections.add(playerConnection);
    }

    // public void 
}
