package com.oing.controller;

import com.oing.dto.request.CreatePostReactionRequest;
import com.oing.restapi.PostReactionApi;
import org.springframework.stereotype.Controller;

@Controller
public class PostReactionController implements PostReactionApi {
    @Override
    public void reactToPost(String postId, CreatePostReactionRequest request) {

    }

    @Override
    public void deletePostReaction(String postId, CreatePostReactionRequest request) {
        
    }
}
