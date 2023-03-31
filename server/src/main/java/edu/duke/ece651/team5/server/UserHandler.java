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

    // TODO: is there any better way?
    boolean logged_in;

    public UserHandler(PlayerConnection playerConnection, UserManager userManager) {
        this.playerConnection = playerConnection;
        this.userManager = userManager;
        this.operationHandlers = new HashMap<>();
        this.logged_in = false;

        // TODO: if add function, it cannot throw the exception out
        // TODO: if I server use enum to replace these, should i ask client to send enum
        // to me?
        this.operationHandlers.put("Login", () -> {
            try {
                handleLogin();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });

        this.operationHandlers.put("Signup", () -> {
            try {
                handleSignUp();
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }
        });
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

        // TODO: redundancy?
        // loop for game
        while (logged_in) {
            String op;
            try {
                op = (String) playerConnection.getObjectInputStream().readObject();
                handleUserOperation(op);
            } catch (ClassNotFoundException | IOException e) {
                // TODO Auto-generated catch block
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
    protected void handleLogin() throws ClassNotFoundException, IOException {
        // read info from client
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
        logged_in = true;
    }

    /**
     * Handle sign up operation
     * 
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    protected void handleSignUp() throws ClassNotFoundException, IOException {
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
    }

}
