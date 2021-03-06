package com.springboot.blog.springbootblogrestapi.service.impl;

import com.springboot.blog.springbootblogrestapi.dto.CommentDto;
import com.springboot.blog.springbootblogrestapi.entity.Comment;
import com.springboot.blog.springbootblogrestapi.entity.Post;
import com.springboot.blog.springbootblogrestapi.exception.BlogAPIException;
import com.springboot.blog.springbootblogrestapi.exception.ResourceNotFound;
import com.springboot.blog.springbootblogrestapi.repository.CommentRepository;
import com.springboot.blog.springbootblogrestapi.repository.PostRepository;
import com.springboot.blog.springbootblogrestapi.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    @Override
    public CommentDto createComment(long postId, CommentDto commentDto) {
       Comment comment = mapToEntity(commentDto);

//       retrive post entity by Id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "id", postId));

//      set post to comment Entity
        comment.setPost(post);

//      save comment to DB
        Comment newComment =  commentRepository.save(comment);

//      convert to commentDTO
        return mapToDTO(newComment);
    }



    @Override
    public List<CommentDto> getCommentsByPostId(long postId) {
//       retrieve comments by postId
       List <Comment> comments = commentRepository.findByPostId(postId);

//       convert list of comment  entities to comment DTO's
        return comments.stream().map(comment -> mapToDTO(comment)).collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentById(Long postId, Long commentId) {

        //       retrieve post entity by Id
        Post post = postRepository.findById(postId).orElseThrow(() -> new ResourceNotFound("Post", "id", postId));
//      retrieve comment by id
       Comment comment = commentRepository.findById(commentId).orElseThrow(()-> new ResourceNotFound("Comment", "comment",commentId));

        if(!comment.getPost().getId().equals(post.getId())){
            throw new BlogAPIException(HttpStatus.BAD_REQUEST, "Comment does not belong to post");
        }

        return mapToDTO(comment);
    }


    private CommentDto mapToDTO(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId());
        commentDto.setEmail(comment.getEmail());
        commentDto.setName(comment.getName());
        commentDto.setBody(comment.getBody());
        return commentDto;
    }

    private Comment mapToEntity(CommentDto commentDto){
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setName(commentDto.getName());
        comment.setEmail(commentDto.getEmail());
        comment.setBody(commentDto.getBody());
        return comment;

    }
}
