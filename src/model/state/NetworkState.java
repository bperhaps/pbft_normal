package model.state;

import network.Network;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NetworkState {
    private Map<String, Boolean> sent = new ConcurrentHashMap<>();
    private int N = 4;

    public int getF() {
        return (N - 1) / 3;
    }

    public int getN() {
        return N;
    }

    public void sentMessage(String identity) {
        sent.put(newIdentity(identity), true);
    }

    public boolean isSentMessage(String identity) {
        return sent.getOrDefault(newIdentity(identity), false);
    }

    private String newIdentity(String identity) {
        String[] attributes = identity.split(Network.DELIMITER);
        String[] newAttributes = Arrays.copyOfRange(attributes,1,attributes.length);

        return String.join(Network.DELIMITER, newAttributes);
    }
}
