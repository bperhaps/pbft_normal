package model.message;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectOutputStream;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class Message {

    public byte[] toBytes() {
        byte[] serializedRequest = serialize();
        ByteBuffer byteBuffer = ByteBuffer.allocate(serializedRequest.length);
        byteBuffer.put(serializedRequest);
        return byteBuffer.array();
    }

    public byte[] getDigest() {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(serialize());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return md.digest();
    }

    protected byte[] serialize() {
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
}