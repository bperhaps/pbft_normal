package network;

import model.message.SignedMessage;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

class Connector {
    private Socket socket;

    public Connector(String host, int port) {
        try {
            socket = new Socket(host, port);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(SignedMessage message) {
        try {
            OutputStream outputStream = socket.getOutputStream();
            outputStream.write(message.toNetworkBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
