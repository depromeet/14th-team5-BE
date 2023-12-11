package com.oing.restapi;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "가족 API")
@RestController
@Valid
@RequestMapping("/api/family")
public interface FamilyApi {
}
