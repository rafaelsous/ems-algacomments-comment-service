package com.rafaelsousa.comment.service.api.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class CommentModerationProhibitedWordException extends RuntimeException {
    public CommentModerationProhibitedWordException(String message) {
        super(message);
    }
}