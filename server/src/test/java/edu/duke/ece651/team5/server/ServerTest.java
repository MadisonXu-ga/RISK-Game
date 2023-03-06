package edu.duke.ece651.team5.server;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team5.shared.RISKMap;

public class ServerTest {
    @Test
    void testSendMapToOneClient() throws IOException, ClassNotFoundException {
        Server server = new Server(6666);

        // connect
        Socket socket = new Socket("vcm-30481.vm.duke.edu", 6666);
        server.start();
        // send
        server.sendMapToOneClient(socket);
        // receive
        InputStream inputStream = socket.getInputStream();
        // ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        
        /*
         * need more info to check
         * !! seems like that out objects need to implement Serializable (the easiest
         * way)
         * or turn into json or other formats (which need third-party serialization
         * library) !!
         */

        // RISKMap map = (RISKMap) objectInputStream.readObject();
        // assertNotNull(map);

        socket.close();
        server.stop();
    }
}
