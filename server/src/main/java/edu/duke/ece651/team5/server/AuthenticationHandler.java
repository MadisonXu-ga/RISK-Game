package edu.duke.ece651.team5.server;

import java.util.HashMap;

public class AuthenticationHandler {
    private HashMap<String, String> logInfo;

    // should i return string or boolean? or i just send msg to clients here?
    public void logIn(String name, String password){
        if(!logInfo.containsKey(name)){
            // first time, need to sign up
        }
        else if(!logInfo.get(name).equals(password)){
            // password is wrong
        }
        else{
            // log in successfully!
        }
    }

    public void signUp(String name, String password){
        if(logInfo.containsKey(name)){
            // 
        }
    }

    // do i need this?
    public void logOut(){

    }
}
