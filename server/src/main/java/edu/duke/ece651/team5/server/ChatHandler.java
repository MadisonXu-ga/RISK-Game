package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.PlayerChatConnection;

public class ChatHandler implements Runnable {
    private PlayerChatConnection playerConnection_chat;
    private HashMap<User, PlayerChatConnection> clientsChat;
    private HashMap<Integer, GameController> allGames;
    private UserGameMap userGameMap;

    public ChatHandler(PlayerChatConnection playerConnection_chat, HashMap<Integer, GameController> allGames,
            UserGameMap userGameMap,
            HashMap<User, PlayerChatConnection> clientsChat) {
        this.playerConnection_chat = playerConnection_chat;
        this.allGames = allGames;
        this.clientsChat = clientsChat;
        this.userGameMap = userGameMap;
    }

    @Override
    public void run() {
        try {
            while (!playerConnection_chat.getSocket().isClosed()) {
                String messageReceived = playerConnection_chat.readString();
                if (messageReceived == null) {
                    continue;
                }
                System.out.println("server received: " + messageReceived);
                String[] words = messageReceived.split(" ");
                String gameID = words[0];
                String destColor = words[1];
                String playerColor = words[2];
                GameController gameController = allGames.get(Integer.parseInt(gameID));
                ArrayList<User> users = userGameMap.getGameUsers(gameController);
                broadcastChat(users, gameID, playerColor, destColor, messageReceived, gameController);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void broadcastChat(ArrayList<User> users, String gameID, String playerColor, String destColor,
            String messageToSend,
            GameController gameController) {
        System.out.println("server broadcast: " + messageToSend);
        for (User user : users) {
            if (gameController.getUserColor(user).equals(destColor)
                    || gameController.getUserColor(user).equals(playerColor)) {
                clientsChat.get(user).writeString(messageToSend);
            }
        }
    }

}
