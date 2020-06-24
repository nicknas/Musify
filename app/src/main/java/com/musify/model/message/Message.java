package com.musify.model.message;

public class Message {
    private String text; // message body
    private boolean belongsToUser; // is this message sent by us?

    public Message(String text, boolean belongsToUser) {
        this.text = text;
        this.belongsToUser = belongsToUser;
    }

    public String getText() {
        return text;
    }

    public boolean isBelongsToUser() {
        return belongsToUser;
    }
}