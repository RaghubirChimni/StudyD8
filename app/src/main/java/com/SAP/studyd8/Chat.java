package com.SAP.studyd8;

public class Chat {
    private String sender, recipient, message;

    public Chat(String s, String r, String m)
    {
        sender = s;
        recipient = r;
        message = m;
    }

    public Chat(){}

    public String getMessage() {
        return message;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }


}
