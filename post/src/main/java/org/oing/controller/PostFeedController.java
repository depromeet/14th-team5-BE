package org.oing.controller;


import com.oing.dto.response.PaginationResponse;
import org.oing.dto.response.PostFeedResponse;
import org.oing.restapi.PostFeedApi;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;

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
        return null;
    }
}
