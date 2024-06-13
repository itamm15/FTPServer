package FTPServer;

import FTPServer.frame.Frame;
import FTPServer.client.Client;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        client.init();

        Frame frame = new Frame(client);
        frame.init();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            client.closeConnections();
        }));
    }
}
