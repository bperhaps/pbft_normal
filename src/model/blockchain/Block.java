package model.blockchain;

import model.message.Request;
import model.message.SignedMessage;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class Block implements Serializable {
    long height;
    byte[][] requests;
    String previousHash;
    String currentHash;

    public Block(long height, Request[] requests, String previousHash) {
        this.height = height;

        this.requests = new byte[requests.length][];
        for (int i = 0; i < requests.length; i++) {
            this.requests[i] = requests[i].toBytes();
        }

        this.previousHash = previousHash;
        this.currentHash = toHash();
    }

    public int size() {
        return requests.length;
    }

    public String getCurrentHash() {
        return currentHash;
    }

    public byte[] getCurrentHashBytes() {
        return Base64.getDecoder().decode(currentHash);
    }

    public byte[] getPreviousHash() {
        return Base64.getDecoder().decode(previousHash);
    }

    public Request[] getDatas() {
        Request[] requests = new Request[this.requests.length];

        for (int i = 0; i < this.requests.length; i++) {
            requests[i] = deSerialize(this.requests[i]);
        }
        return requests;
    }

    public long getHeight() {
        return height;
    }

    public byte[] toBytes() {
        return serialize();
    }

    private String toHash() {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return Base64.getEncoder().encodeToString(messageDigest.digest(serialize()));
    }

    private byte[] serialize() {
        byte[] serializedRequest = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            try (ObjectOutputStream oos = new ObjectOutputStream(baos)) {
                oos.writeObject(this);
                serializedRequest = baos.toByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return serializedRequest;
    }

    private Request deSerialize(byte[] data) {
        Request request = null;

        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data));
            request = (Request) objectInputStream.readObject();
        } catch(IOException | ClassNotFoundException invalidClassException) {
            invalidClassException.printStackTrace();
        }

        return request;
    }
}
