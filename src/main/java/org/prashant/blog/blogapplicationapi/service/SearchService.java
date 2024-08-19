package org.prashant.blog.blogapplicationapi.service;

import org.prashant.blog.blogapplicationapi.payload.SearchResultPageResponse;
import org.prashant.blog.blogapplicationapi.payload.SearchRequest;
import org.prashant.blog.blogapplicationapi.payload.SearchSuggestionResponse;

public interface SearchService {
    SearchSuggestionResponse getSuggestions(SearchRequest req);
    SearchResultPageResponse getSearchResult(SearchRequest req);
}
