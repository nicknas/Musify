package com.musify.model.message;

/**
 * Clase que representa los mensajes intercambiados
 *
 */
public class Message {
    private String text;
    private boolean belongsToUser;

    /**
     * Constructor encargado de generar un nuevo mensaje
     * @param text, un string que representa el mensaje
     * @param belongsToUser, si el mensaje lo introdujo el usuario o el chatbot
     */
    public Message(String text, boolean belongsToUser) {
        this.text = text;
        this.belongsToUser = belongsToUser;
    }

    /**
     * Devuelve el texto que lleva el mensaje
     * @return un String que representa el mensaje
     */
    public String getText() {
        return text;
    }

    /**
     * Devuelve si el mensaje pertenece al usuario o no
     * @return booleano que representa si quien ha mandado el mensaje ha sido el usuario o el Chatbot
     */
    public boolean isBelongsToUser() {
        return belongsToUser;
    }
}