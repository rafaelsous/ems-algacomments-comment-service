package com.rafaelsousa.comment.service.api.client.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class CommentModerationClientBadGatewayException extends RuntimeException {

}