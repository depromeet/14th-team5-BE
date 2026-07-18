package com.oing.restapi;

import com.oing.util.security.LoginFamilyId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Tag(name = "가족 사진 다운로드 API", description = "가족 사진 다운로드 관련 API")
@RestController
@Valid
@RequestMapping("/v1/family-photos")
public interface FamilyPhotoDownloadApi {

    @Operation(summary = "가족 사진 전체 다운로드", description = "가족 구성원이 업로드한 모든 사진을 ZIP 파일로 다운로드합니다.")
    @GetMapping(value = "/download", produces = "application/zip")
    ResponseEntity<StreamingResponseBody> downloadFamilyPhotos(
            @Parameter(hidden = true)
            @LoginFamilyId
            String loginFamilyId
    );
}
