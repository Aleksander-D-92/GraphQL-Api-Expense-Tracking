package com.expence_tracking.app.services.comment;

import com.expence_tracking.app.domain.Comment;
import com.expence_tracking.app.domain.User;
import com.expence_tracking.app.dto.binding.comment.CommentCreate;
import com.expence_tracking.app.dto.binding.comment.CommentEdit;
import com.expence_tracking.app.dto.view.CommentView;
import com.expence_tracking.app.dto.view.Message;
import com.expence_tracking.app.repostiories.CommentRepository;
import com.expence_tracking.app.repostiories.UserRepository;
import graphql.kickstart.tools.GraphQLMutationResolver;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Service
@Validated
@AllArgsConstructor
public class CommentMutationService implements GraphQLMutationResolver
{
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;

    public CommentView createComment(@Valid  CommentCreate form)
    {
        User submitter = userRepository.getOne(form.getUserId());
        Comment newComment = this.modelMapper.map(form, Comment.class);
        newComment.setCreationDate(LocalDateTime.now());
        newComment.setSubmitter(submitter);
        Long newCommentId = this.commentRepository.save(newComment).getCommentId();

        Comment savedComment = this.commentRepository.findByCommentId(newCommentId);
        CommentView commentVew = this.modelMapper.map(savedComment, CommentView.class);
        commentVew.setSubmitterId(savedComment.getSubmitter().getUserId());
        return commentVew;
    }

    public Message deleteComment(@NotNull Long id)
    {
        this.commentRepository.deleteById(id);
        return new Message("Successfully deleted comment");
    }

    public CommentView updateComment(@Valid CommentEdit form)
    {
        Comment comment = this.commentRepository.findByCommentId(form.getCommentId());
        assert comment != null;
        comment.setTitle(form.getTitle());
        comment.setDescription(form.getDescription());
        this.commentRepository.save(comment);

        CommentView commentVew = this.modelMapper.map(comment, CommentView.class);
        commentVew.setSubmitterId(comment.getSubmitter().getUserId());
        return commentVew;
    }
}