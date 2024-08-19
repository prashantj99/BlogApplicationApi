package org.prashant.blog.blogapplicationapi.payload;

import java.util.List;

public record SearchSuggestionResponse(List<String> suggestions) {
}
