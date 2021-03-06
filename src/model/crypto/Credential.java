package model.crypto;

import model.message.Message;
import model.message.SignedMessage;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

public class Credential {
    private KeyPair keyPair;
    private Cipher encCipher;
    private static Cipher decCipher;
    private String name;
    public long primaryNumber;

    static {
        try {
            decCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        }
    }

    public Credential(String name, long primaryNumber) {
        KeyPairGenerator gen = null;
        try {
            this.encCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            gen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }
        gen.initialize(1024, new SecureRandom());
        this.keyPair = gen.generateKeyPair();
        this.name = name;
        this.primaryNumber = primaryNumber;
    }

    public PublicKey getPublicKey() {
        return keyPair.getPublic();
    }

    public long getPrimaryNumber() {
        if(primaryNumber < 0) {
            throw new NullPointerException("프라이머리 설정해");
        }

        return primaryNumber;
    }


    public String getName() {
        if(name == null) {
            throw new NullPointerException("이름 정하세요");
        }
        return name;
    }


    public synchronized byte[] sign(Message message) {
        byte[] signature = null;

        try {
            encCipher.init(Cipher.ENCRYPT_MODE, keyPair.getPrivate());
            byte[] digest = message.getDigest();
            signature = encCipher.doFinal(digest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return signature;
    }

    public static synchronized boolean validation(SignedMessage signedMessage, PublicKey publicKey) {
        try {
            decCipher.init(Cipher.DECRYPT_MODE, publicKey);
            return Objects.equals(Base64.getEncoder().encodeToString(signedMessage.getMessageDigest()),
            Base64.getEncoder().encodeToString(decCipher.doFinal(signedMessage.getSignature())));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}