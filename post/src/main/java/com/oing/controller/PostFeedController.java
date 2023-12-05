package com.oing.controller;


import com.oing.dto.response.PaginationResponse;
import com.oing.dto.response.PostFeedResponse;
import com.oing.restapi.PostFeedApi;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/12/05
 * Time: 12:24 PM
 */
@Controller
public class PostFeedController implements PostFeedApi {
    @Override
    public PaginationResponse<PostFeedResponse> fetchDailyFeeds(Integer page, Integer size, LocalDate date) {
        if (page > 5) return new PaginationResponse<>(page, 5, size, List.of());

        String postIdBase = "01HGW2N7EHJVJ4CJ999RRS2E";
        String writerIdBase = "01HGW2N7EHJVJ4CJ888RRS2E";

        List<PostFeedResponse> mockResponses = new ArrayList<>();
        Random random = new Random();
        for(int i = 0; i < size; i++) {
            int currentIndex = i + ((page - 1) * size);
            String suffix = String.format("%02d", currentIndex);
            mockResponses.add(
                    new PostFeedResponse(
                            postIdBase + suffix,
                            writerIdBase + suffix,
                            random.nextInt(5),
                            random.nextInt(5),
                            "https://picsum.photos/200/300?random=" + currentIndex,
                            ZonedDateTime.now().minusSeconds(currentIndex * 30L)
                    )
            );
        }

        return new PaginationResponse<>(page, 5, size, mockResponses);
    }
}
