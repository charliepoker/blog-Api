package com.springboot.blog.springbootblogrestapi.service;

import com.springboot.blog.springbootblogrestapi.dto.CommentDto;

public interface CommentService {

    CommentDto createComment(long postId, CommentDto commentDto);
}
