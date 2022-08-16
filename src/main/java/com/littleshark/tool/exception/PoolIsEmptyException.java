package com.littleshark.tool.exception;

public class PoolIsEmptyException extends RuntimeException{
    public PoolIsEmptyException() {
    }

    public PoolIsEmptyException(String msg){
        super(msg);
    }
}
