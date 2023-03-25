package edu.duke.ece651.team5.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public abstract class ConnectionHandler implements Runnable {

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    /**
     * @param oos ObjectOutputStream
     * @param ois ObjectInputStream
     */
    public ConnectionHandler(ObjectOutputStream oos, ObjectInputStream ois) {
        this.objectOutputStream = oos;
        this.objectInputStream = ois;
    }

    /**
     * @param ob Object
     * @throws IOException
     */
    public void sendObject(Object ob) throws IOException {
        this.objectOutputStream.reset();
        this.objectOutputStream.writeObject(ob);
        this.objectOutputStream.flush();
    }

    /**
     * @return Object result
     * @throws ClassNotFoundException
     * @throws IOException
     */
    public Object recvObject() throws ClassNotFoundException, IOException {
        Object result = this.objectInputStream.readObject();
        return result;
    }
}
