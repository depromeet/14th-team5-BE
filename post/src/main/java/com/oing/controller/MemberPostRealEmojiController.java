package com.oing.controller;


import com.oing.domain.MemberPost;
import com.oing.domain.PaginationDTO;
import com.oing.dto.request.CreatePostRequest;
import com.oing.dto.request.PostRealEmojiRequest;
import com.oing.dto.request.PreSignedUrlRequest;
import com.oing.dto.response.*;
import com.oing.exception.DuplicatePostUploadException;
import com.oing.exception.InvalidUploadTimeException;
import com.oing.restapi.MemberPostApi;
import com.oing.restapi.MemberPostRealEmojiApi;
import com.oing.service.MemberBridge;
import com.oing.service.MemberPostService;
import com.oing.util.AuthenticationHolder;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.util.Collections;

@RequiredArgsConstructor
@Controller
public class MemberPostRealEmojiController implements MemberPostRealEmojiApi {

    @Override
    public DefaultResponse createRealEmoji(String postId, PostRealEmojiRequest request) {
        return new DefaultResponse(true);
    }

    @Override
    public DefaultResponse deleteRealEmoji(String postId, String realEmojiId) {
        return new DefaultResponse(true);
    }

    @Override
    public ArrayResponse<PostRealEmojiResponse> getPostRealEmojis(String postId) {
        return ArrayResponse.of(Collections.emptyList());
    }
}
