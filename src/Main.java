import model.blockchain.service.BlockGenerator;
import model.crypto.Credential;
import model.message.Request;
import model.message.SignedMessage;
import model.state.States;
import network.Network;
import network.handler.Operation;
import repository.Repositories;

import java.util.Arrays;

public class Main {

    static class Node {
        private Credential credential;
        private Repositories repositories;
        private Network network;
        private States states;
        private int port;

        public Node(String name, long primaryNumber) {
            this.credential = new Credential(name, primaryNumber);
            this.repositories = new Repositories();
            this.network = new Network();
            this.states = new States();
        }

        public void start(int port) {
            this.port = port;
            network.listen(port, repositories, states, credential);
        }

        public void connect(Node[] nodes) {
            for (Node node : nodes) {
                if (!node.equals(this)) {
                    network.connectTo("localhost", node.port);
                }
            }
        }

        public void sendRequest(SignedMessage signedMessage) {
//            network.broadcast(signedMessage);
            new Thread(() -> Operation.handle(signedMessage, network, states, repositories, credential)).start();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int nodeNumber = 4;

        Node[] nodes = new Node[nodeNumber];

        for (int i = 0; i < nodeNumber; i++) {
            nodes[i] = new Node(String.format("node%d", i), i);
            nodes[i].start(8080 + i);
        }

        for (int i = 0; i >= 0; i--) {
            System.out.println("since " + i);
            Thread.sleep(1000);
        }

        for (int i = 0; i < nodeNumber; i++) nodes[i].connect(nodes);

        for (int i = 0; i < nodeNumber; i++) {
            for (int j = 0; j < nodeNumber; j++) {
                nodes[i].repositories.publicKey().add(nodes[j].credential.getName(), nodes[j].credential.getPublicKey());
            }
        }

        BlockGenerator.start = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Request request = Request.of("hello" + i, nodes[0].credential);
            SignedMessage signedMessage = SignedMessage.of(request, nodes[0].credential);
            nodes[0].sendRequest(signedMessage);
        }
    }



/*
    private static Request createNewRequest(Client client, String message) {
        Request request = Request.valueOf(client.name, message);
        SignedMessage signedMessage = new SignedMessage(request, client.keypair.getPrivate())
    }

    private static Client createNewClient(String name) {
        KeyPair keyPair = generateKeyPair();
        Repositories.getInstance().publicKey().add(name, keyPair.getPublic());
        return new Client(name, keyPair);
    }

    private static KeyPair generateKeyPair() {
        KeyPairGenerator gen = null;
        try {
            gen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        gen.initialize(1024, new SecureRandom());
        return gen.generateKeyPair();
    }*/

}
