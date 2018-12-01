package com;

public class DelayException extends Exception {

    private final long HOW_LONG;

    public DelayException(long howLong) {
        HOW_LONG = howLong;
    }

    public long getHOW_LONG() {
        return HOW_LONG;
    }
}
