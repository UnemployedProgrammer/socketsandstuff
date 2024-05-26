package com.sebastian.sockets.misc;

public enum ConnectionState {
    ALREADY_CONNECTED_DEVICE("device_connected", true),
    ALREADY_CONNECTED_SOCKET("socket_connected", true),
    BE_NOT_FOUND("be_not_found", true),
    CONNECTION_ERROR("error", true),
    SUCCESS("success", false),
    BROKEN("broken", false),
    COULD_NOT_BREAK("break_fail", true),
    SHIFT_TO_BREAK("shifttobreak", true),
    NOT_CONNECTED("not_connected", true);

    private String name;
    private Boolean error;

    ConnectionState(String name, Boolean error) {
        this.name = name;
        this.error = error;
    }

    public String getName() {
        return name;
    }

    public Boolean getError() {
        return error;
    }
}
