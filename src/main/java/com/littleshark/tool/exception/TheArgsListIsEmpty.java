package com.littleshark.tool.exception;

public class TheArgsListIsEmpty extends RuntimeException{
    public TheArgsListIsEmpty() {
    }

    public TheArgsListIsEmpty(String msg) {
        super(msg);
    }
}
