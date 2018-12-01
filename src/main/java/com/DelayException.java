package com;

class DelayException extends Exception {

    private final long HOW_LONG;

    DelayException(long howLong) {
        HOW_LONG = howLong;
    }

    long getHOW_LONG() {
        return HOW_LONG;
    }
}
