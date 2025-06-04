package com.rafaelsousa.comment.service.api.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CommentNotFoundException extends ResponseStatusException {

    public CommentNotFoundException(HttpStatusCode status) {
        super(status);
    }
}