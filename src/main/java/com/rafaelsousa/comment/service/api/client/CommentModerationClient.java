package com.rafaelsousa.comment.service.api.client;

import com.rafaelsousa.comment.service.api.model.CommentModerationInput;
import com.rafaelsousa.comment.service.api.model.CommentModerationOutput;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/moderate")
public interface CommentModerationClient {

    @PostExchange
    CommentModerationOutput moderate(@RequestBody CommentModerationInput input);
}