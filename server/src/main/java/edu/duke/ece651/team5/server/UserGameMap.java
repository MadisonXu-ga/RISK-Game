package edu.duke.ece651.team5.server;

import java.util.ArrayList;
import java.util.HashMap;

public class UserGameMap {
    private HashMap<User, ArrayList<GameController>> userToGame;
    private HashMap<GameController, ArrayList<User>> gameToUser;

    public UserGameMap() {
        this.userToGame = new HashMap<>();
        this.gameToUser = new HashMap<>();
    }

    /**
     * Add new user to specific game
     * 
     * @param game the game that user joined
     * @param user the user who joined the game
     */
    public void addUserToGame(GameController game, User user) {
        if (gameToUser.containsKey(game)) {
            gameToUser.get(game).add(user);
        } else {
            gameToUser.put(game, new ArrayList<>());
            gameToUser.get(game).add(user);
        }
    }

    /**
     * Add new game to specific user
     * 
     * @param user the game that user joined
     * @param game the user who joined the game
     */
    public void addGameToUser(User user, GameController game) {
        if (userToGame.containsKey(user)) {
            userToGame.get(user).add(game);
        } else {
            userToGame.put(user, new ArrayList<>());
            userToGame.get(user).add(game);
        }
    }

    /**
     * Get specific user's games
     * 
     * @param user specifi user
     * @return list of games
     */
    public ArrayList<GameController> getUserGames(User user) {
        if (userToGame.containsKey(user)) {
            return userToGame.get(user);
        }
        return null;
    }

    /**
     * Get specific game's users
     * 
     * @param game specifi game
     * @return list of users
     */
    public ArrayList<User> getGameUsers(GameController game) {
        if (gameToUser.containsKey(game)) {
            return gameToUser.get(game);
        }
        return null;
    }

    /**
     * Delete map between user and game
     * 
     * @param user the user in the map
     * @param game the game in the map
     */
    public void deleteMap(User user, GameController game) {
        userToGame.get(user).remove(game);
        gameToUser.get(game).remove(user);
    }

    public boolean checkUserinGame(User user, GameController game) {
        return getGameUsers(game).contains(user);
    }
}
