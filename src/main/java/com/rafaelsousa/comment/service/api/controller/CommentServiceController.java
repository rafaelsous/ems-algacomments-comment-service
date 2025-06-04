package com.rafaelsousa.comment.service.api.controller;

import com.rafaelsousa.comment.service.api.client.CommentModerationClient;
import com.rafaelsousa.comment.service.api.client.exception.CommentModerationProhibitedWordException;
import com.rafaelsousa.comment.service.api.client.exception.CommentNotFoundException;
import com.rafaelsousa.comment.service.api.model.CommentInput;
import com.rafaelsousa.comment.service.api.model.CommentModerationInput;
import com.rafaelsousa.comment.service.api.model.CommentModerationOutput;
import com.rafaelsousa.comment.service.api.model.CommentOutput;
import com.rafaelsousa.comment.service.common.GeneratorID;
import com.rafaelsousa.comment.service.domain.model.Comment;
import com.rafaelsousa.comment.service.domain.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentServiceController {
    private final CommentRepository commentRepository;
    private final CommentModerationClient commentModerationClient;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentOutput create(@RequestBody CommentInput input) {
        Comment comment = Comment.builder()
                .id(GeneratorID.generateTimeBasedUUID())
                .text(input.getText())
                .author(input.getAuthor())
                .createdAt(java.time.OffsetDateTime.now())
                .build();

        moderateComment(comment);

        comment = commentRepository.saveAndFlush(comment);

        return convertToModel(comment);
    }

    @GetMapping("/{id}")
    public CommentOutput getOne(@PathVariable UUID id) {
        Comment comment = findOrFail(id);

        return convertToModel(comment);
    }

    @GetMapping
    public Page<CommentOutput> search(@PageableDefault Pageable pageable) {
        Page<Comment> commentPage = commentRepository.findAll(pageable);

        return commentPage.map(this::convertToModel);
    }

    private Comment findOrFail(UUID id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(HttpStatus.NOT_FOUND));
    }

    private CommentOutput convertToModel(Comment comment) {
        return CommentOutput.builder()
                .id(comment.getId())
                .text(comment.getText())
                .author(comment.getAuthor())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    private void moderateComment(Comment comment) {
        CommentModerationOutput commentModerationOutput = commentModerationClient
                .moderate(CommentModerationInput.builder()
                        .text(comment.getText())
                        .commentId(comment.getId())
                        .build());

        if (Boolean.FALSE.equals(commentModerationOutput.getApproved())) {
            throw new CommentModerationProhibitedWordException("Comment not approved. "
                    + commentModerationOutput.getReason());
        }
    }
}