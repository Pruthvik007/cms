package com.app.cms.pojos;

import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@ToString
public class Response<T> {
    private T data;
    private String message;
    private Status status;

    public enum Status {
        SUCCESS, FAILURE, ERROR
    }
}
