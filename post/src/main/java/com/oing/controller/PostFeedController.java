package com.oing.controller;

import com.oing.restapi.PostFeedApi;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

@Controller
public class PostFeedController implements PostFeedApi {

    @Override
    public ResponseEntity<Boolean> getIsTodayFeedUploadedByUserId(String userId) {
        return ResponseEntity.ok(false);
    }
}
