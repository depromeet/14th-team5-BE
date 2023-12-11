package com.oing.restapi;

import com.oing.dto.request.CreatePostReactionRequest;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@Tag(name = "게시물 반응 API", description = "게시물 반응 관련 API")
@RestController
@Valid
@RequestMapping("/v1/posts/{postId}/reactions")
public interface PostReactionApi {
    @PostMapping
    void reactToPost(
            @Parameter(description = "게시물 ID", example = "01HGW2N7EHJVJ4CJ999RRS2E97")
            @PathVariable
            String postId,

            @RequestBody
            CreatePostReactionRequest request
    );
}
