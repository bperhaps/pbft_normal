package model.message;

import model.blockchain.Block;
import model.crypto.Credential;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Base64;

public class SignedMessage extends Message implements Serializable {
    String sender;
    String message;
    String signature;
    byte[] piggiBack;
    private Object Message;

    private SignedMessage(Message message, Credential credential) {
        this.message = Base64.getEncoder().encodeToString(message.toBytes());
        this.signature = Base64.getEncoder().encodeToString(credential.sign(message));
        this.sender = credential.getName();
        this.piggiBack = new byte[]{};
    }

    private SignedMessage(Message message, Block piggiback, Credential credential) {
        this(message, credential);
        this.piggiBack = piggiback.toBytes();
    }

    public static SignedMessage of(Message message, Credential credential) {
        return new SignedMessage(message, credential);
    }

    public static SignedMessage of(Message message, Block piggiBack, Credential credential) {
        return new SignedMessage(message, piggiBack, credential);
    }

    public Message getMessage() {
        try {
            return (Message) deSerialize(Base64.getDecoder().decode(message));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public byte[] getMessageDigest() {
        return getMessage().getDigest();
    }

    public byte[] toNetworkBytes() {
        byte[] serializedRequest = toBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(serializedRequest.length + 4);
        byteBuffer.putInt(serializedRequest.length);
        byteBuffer.put(serializedRequest);
        return byteBuffer.array();
    }

    public byte[] getSignature() {
        return Base64.getDecoder().decode(signature);
    }

    public Block getPiggiBack() {
        try {
            return (Block)deSerialize(piggiBack);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getSenderName() {
        return sender;
    }

    private <T> T deSerialize(byte[] data) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream =
                new ObjectInputStream(new ByteArrayInputStream(data));
        T message = null;
        try {
            message = (T) objectInputStream.readObject();
        } catch (InvalidClassException invalidClassException) {
            invalidClassException.printStackTrace();
        }

        return message;
    }
}
