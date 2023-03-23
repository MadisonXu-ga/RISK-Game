package edu.duke.ece651.team5.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.*;

//todo change AttackOrder type to AttackOrder(now Integer)
/*
 * TextPlayer is responsible for the interaction between user and client side
 * It will do format check on user input and generate corrresponding message to client 
 * that ready to be sent to server
 */
public class TextPlayer {
  private String playerName;

  private final BufferedReader inputReader;
  private final PrintStream out;

  /**
   * constructor to call with
   * @param br bufferReader to read input
   * @param out PrintStream for output
   */
  public TextPlayer(BufferedReader br, PrintStream out){
    this.inputReader = br;
    this.out = out;
  }

  /**
   * Get Player Name
   * @return playerName
   */
  public String getPlayerName(){
    return playerName;
  }

  /**
   * If player is the first one enter the game, she/he is responsible to select the number of player in this game
   * and will do a format and bound check for user input
   * @return a int for the user selection 
   */
  public int selectNumPlayer(){
    out.println("Seems like you are the first player in the game!");
    String instruction = "Please first enter how many players you want to play with(from 2 to 4 inclusive)\n";
    int numPlayer = parseNumFromUsr(instruction, 2, 4,1);
    return numPlayer;
  }

  /**
   * Set the player name 
   * @param playerName
   */
  public void setPlayerName(String playerName){
    out.println("This is your player name for this game: " + playerName);
    this.playerName = playerName;
  }

  /**
   * Let user select how many unit wants to place in each territory
   * and will do a format check and bound check for user each input
   * @param currMap the map for the game 
   * @return a HashMap with key of Territory Name and value of number of desired unit
   */
  public HashMap<String, Integer> unitPlacement(RISKMap currMap){
    // out.println("Begin placement");
    Player player = currMap.getPlayerByName(getPlayerName());
    // out.println("Get player: " + player.getName());
    int availableUnit = player.getAvailableUnit();
    int numTerries = player.getTerritories().size();
    // out.println("unit: " + availableUnit + " numTerries: " + numTerries);
    int count = 0;
    HashMap<String, Integer> placementInfo = new HashMap<>();
    //iterate each territory this player is owned
    // out.println("Begin looping territories");
    for(Territory t: player.getTerritories()){
      //if player do not have enough unit or is select last territory, will assign automatically
      if(count == numTerries-1){
        // out.println("Available unit = 0 or last terri");
        placementInfo.put(t.getName(), availableUnit);
        continue;
      }
      String instruction = "How many unit you want to place in your " + t.getName();
      int placeUnit = parseNumFromUsr(instruction, 0, availableUnit, 0);
      // out.println("place info: " + placeUnit + " " + t.getName());
      placementInfo.put(t.getName(), placeUnit);
      //update available unit number
      availableUnit -= placeUnit;
      count++;
    }
    // out.println("finish placement");
    return placementInfo;
  }

  /**
   * Responsible to print the placement result to player
   * @param unitApprove approvement from server
   */
  public void printPlacementResult(boolean unitApprove){
    if(unitApprove){
      out.println("Great! All your placement choices get approved!");
    }else{
      out.println("Sorry your unit placement is not successful, please give another try.");
    }
  }

  /**
   * Display the current map and ask player to select order type and do format check for user's input
   * and collect orders and ready to be sent to server
   * @param currMap the map for current Game
   * @return a Action objects contains all the orders user want to place for current turn
   */
  public Action playOneTurn(RISKMap currMap){
    displayMap(currMap);
    String instruction = "What would you like to do?\n" + "(M)ove\n" + "(A)ttack\n" + "(D)one\n";
    boolean commit = false;
     //todo change type
    ArrayList<AttackOrder> attackOrders = new ArrayList<>();
    ArrayList<MoveOrder> moveOrders = new ArrayList<>();
    while(!commit){
      try{
        //read user input and do type check
        String type = readUserInput(instruction);
        typeCheck(type);
        if(type.equalsIgnoreCase("D") || type.equalsIgnoreCase("done")){
          commit = true;
        }else{
          //create new Order
          tryCreateNewOrder(type, attackOrders, moveOrders, currMap); 
        }
      }catch (Exception e){
        out.println("Input is invalid. Please try again");
      }
    }
    return new Action(attackOrders, moveOrders);
  }


  private void tryCreateNewOrder(String type, ArrayList<AttackOrder> attackOrders, ArrayList<MoveOrder> moveOrders, RISKMap currMap){
    String instruction = "Please enter your unit number of units to " 
    + type + ", the source territory, and the destination territory.\n"
    + "Please separate them by dash(-). For example: 3-TerritoryA-TerritoryB\n"; 
    boolean check = false;
    do{
      try{
        String input = readUserInput(instruction);
        ArrayList<String> inputs = parseUserInput(input);
        // int numUnit = getNumUnit(inputs);
        int numUnit = Integer.parseInt(inputs.get(0));
        //get Territory info
        // String srcTerri = getSrcTerri(inputs, currMap).getName();
        String srcTerri = currMap.getTerritoryByName(inputs.get(1)).getName();
        // String desTerri = getDesTerri(inputs, currMap).getName();
        String desTerri  = currMap.getTerritoryByName(inputs.get(2)).getName();
        if(type.toUpperCase().charAt(0) == 'M'){
          //create new move order
          MoveOrder order = new MoveOrder(srcTerri, desTerri, numUnit, UnitType.SOLDIER, playerName);
          //do rule check
          // MoveOwnershipRuleChecker ruleCheck = new MoveOwnershipRuleChecker(new UnitNumberRuleChecker(new MovePathWithSameOwnerRuleChecker(null)));
          // // String msg = ruleCheck.checkOrder(order, currMap.getPlayerByName(playerName), currMap);
          // if(msg!=null){
          //   out.println(msg);
          //   return;
          // }else{
           //add order to moveOrders
            // check = true;
            moveOrders.add(order);
            return;
          // }
          //throw Illegal Exception if not success
        }else{
          //create new attack order
          AttackOrder order = new AttackOrder(srcTerri, desTerri, numUnit, UnitType.SOLDIER, playerName);
          //add order to attackOrders
          // AttackOwnershipRuleChecker ruleChecker = new AttackOwnershipRuleChecker(new AdjacentRuleChecker(null));
          // String msg =ruleChecker.checkOrder(order, currMap.getPlayerByName(playerName), currMap);
          // if(msg!=null){
          //   out.println(msg);
          //   return;
          // }else{
           //add order to moveOrders
            // check = true;
            attackOrders.add(order);
          //   return;
          // }
          // return;
        }
      }catch(Exception e){
        out.println("Not a valid input, please try again");
      }
    }while(!check);
  }

  /**
   * check if in current round, if any player wins the game
   * @param result the result received from server
   * @return if any player win, return the name of player
   *         else, return null
   */
  public String checkWinner(HashMap<String, Boolean> result){
    String winner = "";
    for(String player: result.keySet()){
      if(result.get(player) != null && result.get(player)){
        winner = player;
        out.println("Player " + winner + " wins!\nGame End NOW");
        break;
      }
    }
    return winner;
  }

  /**
   * Check if player lose the game
   * @param result the result received from server
   * @return if not lose, return blank
   *        if lose, send string message to tell server if want to continue to watch the game or quit
   */
  public String checkIfILose(HashMap<String, Boolean> result){
    String response = "";
    if(result.get(this.playerName)!= null && !result.get(this.playerName)){
      String instruction = "Sorry Player " + this.playerName + ", you lose for this Game.\n" 
                + "Now you have two options:\n"
                + "1. Continue to watch the game\n"
                + "2. Quit the game\n"
                + "Please enter 1 or 2\n";
      int answer = parseNumFromUsr(instruction, 1, 2, 1);
      response = (answer == 1) ? "Display" : "Disconnect";
    }
    return response;
  }


  /**
   * Responsible to print the commit result to player
   * @param commitApprove approvement sent from server
   */
  public void printCommitResult(boolean commitApprove){
    if(commitApprove){
      out.println("You successfully commit all your orders!");
    }else{
      out.println("Sorry your commit is not successful, please give another try.");
    }
  }

  public void printAttackResult (ArrayList<AttackOrder> attRes){
    if(attRes == null){ return; }
    for (AttackOrder order: attRes){
      if(order. getNumber() == 0) {
        out.println("Your attack order to attack Territory " + order.getDestinationName() + " was lose in last round.");
      }else{
      out.println( "Congratulations! You successfully own the Territory " + order.getDestinationName());
      }
    }
  }

  /**
   * display map info to player
   * @param currMap the current map client received from server
   */
  void displayMap(RISKMap currMap){
    MapTextView view = new MapTextView(currMap);
    out.println(view.displayMap());
  }

  /**
   * helper method to parse number from user input, it will also check formate and bound
   * if user input is not correct, it will ask user to conitnue type input untill pass
   * @param instruction a string with instruction for user
   * @param lowerBound a lower bound to do bound check
   * @param upperBound a upper bound to do bound check
   * @return return a valid int number inside the range
   */
  private int parseNumFromUsr(String instruction, int lowerBound, int upperBound, int type){
    boolean status = false;
    int res = 0;
    while(!status){
      try{
        String inputUnit = readUserInput(instruction);
        res = Integer.parseInt(inputUnit);
        if(res < lowerBound || res > upperBound){
          out.println("Number input out of range. Please try again.");
          continue;
        }
        if(type == 0 && upperBound-res == 0){
          continue;
        }
        status = true;
      } catch(Exception e) {
        out.println("Not a valid number input. Please try again");
        // res = parseNumFromUsr(instruction, lowerBound, upperBound);
        // continue;
      }
    }
    return res;
  }

  /**
   * helper method to read input from terminal
   * @param prompt the helper instruction message
   * @return a string contained user input
   * @throws IOException will throw exception is msg is null
   * @throws InterruptedException
   */
  private String readUserInput(String prompt) throws IOException{
    out.println(prompt);
    String msg = inputReader.readLine();
    if (msg == null)
      throw new IOException("Invalid Format: input cannot be null");
    return msg;
  }

  /**
   * helper method to check if user has correct type input
   * @param type the input to be checked
   * @throws IllegalArgumentException will throw if user input is not within the correct choices
   */
  private void typeCheck(String type){
    if(!(type.equalsIgnoreCase("D") || type.equalsIgnoreCase("done")
    ||type.equalsIgnoreCase("M") || type.equalsIgnoreCase("move")
    ||type.equalsIgnoreCase("A") || type.equalsIgnoreCase("attack"))){
      throw new IllegalArgumentException("Invalid Type Input\n");
    }
  }

  /**
   * helper method to parse user input of move order input (source, destination, unit number)
   * @param input the input to be parsed
   * @return a string arraylist contains each info
   */
  public ArrayList<String> parseUserInput(String input){
    ArrayList<String> res = new ArrayList<>();
    int firstParse = input.indexOf("-");
    res.add(input.substring(0, firstParse));
    int secondParse = input.indexOf("-", firstParse+1);
    res.add(input.substring(firstParse+1, secondParse));
    res.add(input.substring(secondParse+1));
    return res;
  }

  // private int getNumUnit(ArrayList<String> inputs){
  //   return Integer.parseInt(inputs.get(0));
  // }


  // private Territory getSrcTerri(ArrayList<String> inputs, RISKMap currMap){
  //   return currMap.getTerritoryByName(inputs.get(1));
  // }


  // private Territory getDesTerri(ArrayList<String> inputs, RISKMap currMap){
  //   return currMap.getTerritoryByName(inputs.get(2));
  // }
 
}
