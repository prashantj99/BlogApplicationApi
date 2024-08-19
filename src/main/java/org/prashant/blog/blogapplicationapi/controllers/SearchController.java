package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.payload.SearchRequest;
import org.prashant.blog.blogapplicationapi.payload.SearchResultPageResponse;
import org.prashant.blog.blogapplicationapi.payload.SearchSuggestionResponse;
import org.prashant.blog.blogapplicationapi.service.SearchService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/search")
public class SearchController {

    public final SearchService searchService;

    // Endpoint for fetching search suggestions
    @GetMapping("/suggestions")
    public SearchSuggestionResponse getSuggestions(@RequestParam("type") String type, @RequestParam("keyword") String keyword) {
        SearchRequest request = new SearchRequest(type, keyword, 0, 30);
        return searchService.getSuggestions(request);
    }

    @GetMapping("/results")
    public SearchResultPageResponse getSearchResults(
            @RequestParam("type") String type,
            @RequestParam("keyword") String keyword,
            @RequestParam(value = "pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize
            ) {
        SearchRequest request = new SearchRequest(type, keyword, pageNumber, pageSize);
        return searchService.getSearchResult(request);
    }
}
