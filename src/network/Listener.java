package network;

import model.crypto.Credential;
import model.message.SignedMessage;
import model.state.States;
import network.handler.Operation;
import repository.Repositories;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;

class Listener {
    private ServerSocket serverSocket;
    private Socket socket;
    private Credential credential;
    private Repositories repositories;
    private Network network;
    private States states;

    public Listener(int port, Network network, States states, Repositories repositories, Credential credential) {
        try {
            this.serverSocket = new ServerSocket(port);
            this.credential = credential;
            this.repositories = repositories;
            this.network = network;
            this.states = states;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        new Thread(this::readInputStreamAndHandle).start();
    }

    private void readInputStreamAndHandle() {
        InputStream inputStream = null;

        while (true) {
            try {
                socket = serverSocket.accept();
                inputStream = socket.getInputStream();

                handle(inputStream);
            } catch (IOException e) {
                System.out.println("accept Error Occur");
                readInputStreamAndHandle();
            }
        }
    }

    private byte[] read(InputStream inputStream) throws IOException {
        int length = raedLength(inputStream);
        return readData(inputStream, length);
    }

    private int raedLength(InputStream inputStream) throws IOException {
        byte[] length = new byte[4];
        inputStream.read(length);
        return ByteBuffer.wrap(length).getInt();
    }

    private byte[] readData(InputStream inputStream, int length) throws IOException {
        byte[] data = new byte[length];
        inputStream.read(data);
        return data;
    }

    public void handle(final InputStream inputStream) {
        new Thread(() -> {
            while (true) {
                try {
                    byte[] data = read(inputStream);
                    SignedMessage message = deSerialize(data);
                    new Thread(() -> Operation.handle(message, network, states, repositories, credential)).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private SignedMessage deSerialize(byte[] data) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream =
                new ObjectInputStream(new ByteArrayInputStream(data));
        SignedMessage signedMessage = null;
        try {
            signedMessage = (SignedMessage) objectInputStream.readObject();
        } catch(InvalidClassException invalidClassException) {
            invalidClassException.printStackTrace();
        }

        return signedMessage;
    }
}
