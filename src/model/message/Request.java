package model.message;

import model.crypto.Credential;

import java.io.Serializable;
import java.util.Date;

public class Request extends Message implements Serializable {
    private String sender;
    private String message;
    private long timestamp;

    private Request(String clientName, String message) {
        this.sender = clientName;
        this.message = message;
        this.timestamp = new Date().getTime();
    }

    public static Request of( String message, Credential credential) {
        return new Request(credential.getName(), message);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        Request request = (Request) o;
        return this.sender.equals(request.sender) && this.message.equals(request.message) && this.timestamp == request.timestamp;
    }

}
