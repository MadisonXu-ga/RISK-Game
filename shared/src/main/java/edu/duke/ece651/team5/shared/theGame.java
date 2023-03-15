package edu.duke.ece651.team5.shared;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class theGame {
    
    private RISKMap theMap;
    private final BufferedReader inputReader;

    public theGame() throws IOException{

        this.inputReader = new BufferedReader(new InputStreamReader(System.in));
        ArrayList<Player> thePlayers = initPlayers();
        this.theMap = new RISKMap(thePlayers);

    }


    public int howManyPlayers() throws IOException{

        

        Integer numberOfPlayers = null;
        String problem = null;
        do {
            problem = null;
            try {
                System.out.println("How many players would you like to have in this game?");
                numberOfPlayers = Integer.parseInt(inputReader.readLine());

                if(numberOfPlayers <= 0){
                    throw new IllegalArgumentException("The number of players can't  be negative or zero");
                }
    
            }

            catch (NumberFormatException iae){
                problem = "This is not a number. Please input a valid input";
            }
            catch (IllegalArgumentException iae){
                problem = iae.getMessage();
            }
            

            if (problem != null) {
                String mesg = "That placement is invalid: " + problem;
                System.out.println(mesg);
            }

        } while (problem != null);

        return numberOfPlayers;
    }

    public ArrayList<Player> initPlayers() throws IOException{

        Integer numberOfPlayers = howManyPlayers();
        ArrayList<Player> thePlayers = new ArrayList<>();
        
        for (int playerX = 0; playerX < numberOfPlayers; playerX++){

            String name = null;
            String problem = null;
            do {
                problem = null;
                try{

                    System.out.println("What name would you like?");
                    name = inputReader.readLine();
                }

                //TODO here we need to add error checking for players that have same name
                catch (EOFException iae){
                    problem = "the name cannot be empty";

                }

            } while (problem != null);

            Player X = new Player(name);
            thePlayers.add(X);
        }

        return thePlayers;
    }
}

    public void assignTerritories(){

        // Iterator<Territory> it
    }