package ru.otus.hw16common.messagesystem;

public enum MessageType {
    USER_DATA("UserData"),
    SAVE_USER("SaveUser");

    private final String value;

    MessageType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
