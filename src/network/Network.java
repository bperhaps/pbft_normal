package network;

import model.crypto.Credential;
import model.message.SignedMessage;
import model.state.States;
import repository.Repositories;

import java.util.HashMap;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class Network {
    public static final String DELIMITER = ":";

    private final ConcurrentHashMap<String, Connector> connectors;

    public Network() {
        connectors = new ConcurrentHashMap<>();
    }

    public void listen(int port, Repositories repositories, States states, Credential credential) {
        Listener listener = new Listener(port, this, states, repositories, credential);
        listener.listen();
    }

    public boolean connectTo(String host, int port) {
        if (hasConnector(host, port)) {
            return false;
        }

        connectors.put(String.join(DELIMITER, host, String.valueOf(port)), new Connector(host, port));
        return true;
    }

    public void broadcast(SignedMessage message) {
        connectors.values()
                .forEach(connector -> connector.send(message));
    }

    public void send(String host, int port, SignedMessage message) {
        Connector connector = getConnector(host, port);
        if (connector == null) {
            System.out.println("Connector Not Found Error");
            return;
        }

        connector.send(message);
    }

    public int connectorSize() {
        return connectors.size();
    }

    private Connector getConnector(String host, int port) {
        return connectors.getOrDefault(
                String.join(DELIMITER, host, String.valueOf(port)),
                null
        );
    }

    private boolean hasConnector(String host, int port) {
        return connectors.keySet().stream()
                .anyMatch((h) -> Objects.equals(
                        h,
                        String.join(DELIMITER, host, String.valueOf(port)))
                );
    }
}
