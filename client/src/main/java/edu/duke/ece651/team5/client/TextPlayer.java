package edu.duke.ece651.team5.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;

import edu.duke.ece651.team5.shared.*;

public class TextPlayer {
  private String playerName;

  private final BufferedReader inputReader;
  private final PrintStream out;

  public TextPlayer(BufferedReader br, PrintStream out){
    this.inputReader = br;
    this.out = out;
    
  }

  public void setPlayerName(String playerName){
    this.playerName = playerName;
  }

  public String getPlayerName(){
    return playerName;
  }

  public HashMap<Territory, Integer> unitPlacement(RISKMap currMap){
    Player player = currMap.getPlayerByName(getPlayerName());
    int availableUnit = player.getAvailableUnit();
    int numTerries = player.getTerritories().size();
    int count = 0;
    HashMap<Territory, Integer> placementInfo = new HashMap<>();
    for(Territory t: player.getTerritories()){
      if(availableUnit <= 0 || count == numTerries-1){
        placementInfo.put(t, availableUnit);
        continue;
      }
      String instruction = "How many unit you want to place in your " + t.getName();
      int placeUnit = parseNumFromUsr(instruction, 0, availableUnit);
      placementInfo.put(t, placeUnit);
      availableUnit -= placeUnit;
    }
    return placementInfo;
  }

  public void printPlacementResult(boolean unitApprove){
    if(unitApprove){
      out.println("You successfully make your placement!");
    }else{
      out.println("Sorry your unit placement is not successful, please give another try.");
    }
  }


  private void tryCreateNewOrder(String type, ArrayList<Integer> attackOrders, ArrayList<MoveOrder> moveOrders, RISKMap currMap){
    String instruction = "Please enter your unit number of units to " 
    + type + ", the source territory, and the destination territory.\n"
    + "Please separate them by dash(-). For example: 3-TerritoryA-TerritoryB\n"; 
    boolean check = false;
    while(!check){
      try{
        String input = readUserInput(instruction);
        ArrayList<String> inputs = parseUserInput(input);
        int numUnit = getNumUnit(inputs);
        //get Territory info
        String srcTerri = getSrcTerri(inputs, currMap).getName();
        String desTerri = getDesTerri(inputs, currMap).getName();
        // if(numUnit < 0){
        //   out.println("Not a valid number input, please try again");
        //   continue;
        // }
        //create new order
        if(type.toUpperCase().charAt(0) == 'M'){
          //create new move order
          MoveOrder order = new MoveOrder(srcTerri, desTerri, numUnit, UnitType.SOLDIER);
          //do rule check
          //add order to moveOrders
          moveOrders.add(order);
          return;
          //throw Illegal Exception if not success
        }else{
          //create new attack order
          //add order to attackOrders
          //throw Illegal Exception if not success
          return;
        }
      }catch(Exception e){
        out.println("Not a valid input, please try again");
      }
    }
  }

  public Action playOneTurn(RISKMap currMap){
    displayMap(currMap);
    //display 3 choices and ask user to choose
    String instruction = "What would you like to do?\n" + "(M)ove\n" + "(A)ttack\n" + "(D)one\n";
    boolean commit = false;
     //todo change type
    ArrayList<Integer> attackOrders = new ArrayList<>();
    ArrayList<MoveOrder> moveOrders = new ArrayList<>();
    while(!commit){
      try{
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

  public void printCommitResult(boolean commitApprove){
    if(commitApprove){
      out.println("You successfully commit all your orders!");
    }else{
      out.println("Sorry your commit is not successful, please give another try.");
    }
  }

  public void displayMap(RISKMap currMap){
    MapTextView view = new MapTextView(currMap);
    out.println(view.displayMap());
  }


  public int selectNumPlayer(){
    out.println("You are the first player in the game!\n");
    String instruction = "Please enter how many players you want to play with(from 2 to 4 inclusive)\n";
    int numPlayer = parseNumFromUsr(instruction, 2, 4);
    return numPlayer;
  }


  private int parseNumFromUsr(String instruction, int lowerBound, int upperBound){
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
        status = true;
      } catch(Exception e) {
        out.println("Not a valid number input. Please try again");
      }
    }
    return res;
  }

  private String readUserInput(String prompt) throws IOException {
    out.println(prompt);
    String type = inputReader.readLine();
    if (type == null)
      throw new IOException("Invalid Format: input cannot be null");
    return type;
  }


  private void typeCheck(String type){
    if(!(type.equalsIgnoreCase("D") || type.equalsIgnoreCase("done")
    ||type.equalsIgnoreCase("M") || type.equalsIgnoreCase("move")
    ||type.equalsIgnoreCase("A") || type.equalsIgnoreCase("attack"))){
      throw new IllegalArgumentException("Invalid Type Input\n");
    }
  }

  public ArrayList<String> parseUserInput(String input){
    ArrayList<String> res = new ArrayList<>();
    int firstParse = input.indexOf("-");
    res.add(input.substring(0, firstParse));
    int secondParse = input.indexOf("-", firstParse+1);
    res.add(input.substring(firstParse+1, secondParse));
    res.add(input.substring(secondParse+1));
    return res;
  }

  private int getNumUnit(ArrayList<String> inputs){
    return Integer.parseInt(inputs.get(0));
  }

  private Territory getSrcTerri(ArrayList<String> inputs, RISKMap currMap){
    return currMap.getTerritoryByName(inputs.get(1));
  }

  private Territory getDesTerri(ArrayList<String> inputs, RISKMap currMap){
    return currMap.getTerritoryByName(inputs.get(2));
  }



  // public boolean selectPlayer() throws IOException, ClassNotFoundException{
  //   // ArrayList<Player> playersList = new ArrayList<>();
  //   //todo
  //   //Iterate playersList to get available players choices
  //   // for(Player p: map.getAllPlayers){
  //   //   playersList.add(p);
  //   // }
  //   // String instruction = "Here are the all available choices of players for you to choose: \n"
  //   //                        + "Enter the player you want to choose: \n";
  //   // boolean status = false;
  //   // while(!status){
  //   //   String name = readUserInput(instruction);
  //   //   boolean verify = false;
  //   //   for(Player player: playersList){
  //   //     if(name.equalsIgnoreCase(player.getName())){
  //   //       //send this name to server to get verification
  //   //       objOutStream.writeObject(name);
  //   //       verify = true;
  //   //       break;
  //   //     }
  //   //   }
  //   //   if(!verify){
  //   //     out.println("Not a valid input, please try again");
  //   //     continue;
  //   //   }
  //     //get verification back from server
  //     //! where to handle if not received a string from server
  //     this.playerName = (String) objInStream.readObject();
  //     if(!playerName.isBlank()){
  //       out.println("You successfully become Player " + playerName + " in this game!");
  //     }else{
  //       out.println("Connecting...");
  //     }
  //   // }
  //   return true;
  // }

 
}
