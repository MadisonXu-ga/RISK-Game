package edu.duke.ece651.team5.server;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class PlayHandler extends ConnectionHandler{

    public PlayHandler(ObjectOutputStream oos, ObjectInputStream ois) {
        super(oos, ois);
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'run'");
    }
    
}
