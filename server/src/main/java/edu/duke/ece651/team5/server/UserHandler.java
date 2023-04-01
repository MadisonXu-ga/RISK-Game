package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.PlayerConnection;
import edu.duke.ece651.team5.server.MyEnum.*;

public class UserHandler implements Runnable {

    private PlayerConnection playerConnection;
    private UserManager userManager;
    private HashMap<String, Runnable> operationHandlers;

    boolean logged_in;

    public UserHandler(PlayerConnection playerConnection, UserManager userManager) {
        this.playerConnection = playerConnection;
        this.userManager = userManager;
        this.operationHandlers = new HashMap<>();
        this.logged_in = false;

        // initializa functions
        this.operationHandlers.put("Login", this::handleLogin);
        this.operationHandlers.put("Signup", this::handleSignUp);
    }

    @Override
    public void run() {
        // try 10 times.
        // put this loop into another function (deal sign in)
        while (!logged_in) {
            try {
                String op = (String) playerConnection.getObjectInputStream().readObject();
                handleUserOperation(op);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }

        // loop for game
        while (logged_in) {
            String op;
            try {
                op = (String) playerConnection.getObjectInputStream().readObject();
                handleUserOperation(op);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        }
    }

    // use enum to replace operation name!

    /**
     * 
     * @param operation
     */
    protected void handleUserOperation(String operation) {
        operationHandlers.get(operation).run();
    }

    /**
     * Handle login operation
     * 
     * @return
     * @throws ClassNotFoundException
     * @throws IOException
     */
    protected void handleLogin() {
        // read info from client
        try {
            ArrayList<String> inputInfo = (ArrayList<String>) playerConnection.readData();
            String inputName = inputInfo.get(0);
            String inputPassword = inputInfo.get(1);

            // check if user exists
            if (!userManager.findUser(inputName)) {
                playerConnection.writeData("Not exists");
                return;
            }

            // authenticate password
            if (!userManager.authenticate(inputName, inputPassword)) {
                playerConnection.writeData("Not match");
                return;
            }

            // success
            playerConnection.writeData("Login succeeded");
            userManager.changeUserStatus(inputName, UserStatus.LOGGED_IN);

            logged_in = true;

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Handle sign up operation
     * 
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected void handleSignUp() {
        try {
            // read info from client
            ArrayList<String> inputInfo = (ArrayList<String>) playerConnection.readData();
            String inputName = inputInfo.get(0);
            String inputPassword = inputInfo.get(1);

            // check if user exists
            if (userManager.findUser(inputName)) {
                playerConnection.writeData("User exists");
                return;
            }

            // success
            userManager.addUser(inputName, inputPassword);
            playerConnection.writeData("Sign up succeeded");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    protected void handleBeginGame() {
        try {
            int playerNum = (int) playerConnection.readData();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
