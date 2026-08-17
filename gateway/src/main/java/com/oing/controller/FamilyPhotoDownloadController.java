package com.oing.controller;

import com.oing.domain.Post;
import com.oing.restapi.FamilyPhotoDownloadApi;
import com.oing.service.FamilyPhotoDownloadService;
import com.oing.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
public class FamilyPhotoDownloadController implements FamilyPhotoDownloadApi {

    private final PostService postService;
    private final FamilyPhotoDownloadService familyPhotoDownloadService;

    @Override
    public ResponseEntity<StreamingResponseBody> downloadFamilyPhotos(String loginFamilyId) {
        log.info("Family {} is trying to download all family photos", loginFamilyId);
        List<Post> posts = postService.findAllByFamilyId(loginFamilyId);

        String fileName = String.format("family-photos-%s.zip", LocalDate.now());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.parseMediaType("application/zip"))
                .body(familyPhotoDownloadService.createFamilyPhotoZipStream(posts));
    }
}
