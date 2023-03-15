package edu.duke.ece651.team5.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class theGame {
    
    private RISKMap theMap;

    public theGame(){

        this.theMap = new RISKMap();

    }


    public RISKMap howManyPlayers() throws IOException{

        BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

        String problem = null;
        do {
            problem = null;
            try {
                System.out.println("How many players would you like to have in this game?");
                Integer numberOfPlayers = Integer.parseInt(input.readLine());

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

        } while (problem != null);
        
        



    }
}
