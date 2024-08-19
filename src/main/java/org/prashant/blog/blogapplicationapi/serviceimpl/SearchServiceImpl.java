package org.prashant.blog.blogapplicationapi.serviceimpl;

import org.prashant.blog.blogapplicationapi.entities.Category;
import org.prashant.blog.blogapplicationapi.entities.Post;
import org.prashant.blog.blogapplicationapi.entities.User;
import org.prashant.blog.blogapplicationapi.payload.*;
import org.prashant.blog.blogapplicationapi.repository.*;
import org.prashant.blog.blogapplicationapi.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public SearchSuggestionResponse getSuggestions(SearchRequest req) {
        Pageable pageable = PageRequest.of(0, 30); // Fetch up to 30 suggestions
        List<String> suggestions = switch (req.type()) {
            case "user" -> userRepository.findUserSuggestions(req.keyword(), pageable).getContent().stream().toList();
            case "blog" -> postRepository.findBlogSuggestions(req.keyword(), pageable).getContent().stream().toList();
            case "tag" -> tagRepository.findTagSuggestions(req.keyword(), pageable).getContent().stream().toList();
            case "category" -> categoryRepository.findCategorySuggestions(req.keyword(), pageable).getContent().stream().toList();
            default -> Collections.emptyList();
        };
        return new SearchSuggestionResponse(suggestions);
    }

    @Override
    public SearchResultPageResponse getSearchResult(SearchRequest req) {
        Pageable pageable = PageRequest.of(req.pageNumber(), req.pageSize());
        Page<?> resultPage = switch (req.type()) {
            case "blog" -> postRepository.findByTitleContainingIgnoreCase(req.keyword(), pageable);
            case "user" -> userRepository.findAllByNameOrAbout(req.keyword(), req.keyword(), pageable);
            case "category" -> categoryRepository.findByTitleContainingIgnoreCase(req.keyword(), pageable);
            case "tag" -> postRepository.findPostsByTagNameContaining(req.keyword(), pageable);
            default -> Page.empty();
        };
        List<?> response = resultPage.getContent().stream().map(obj -> {
            if(obj instanceof Post){
                return new PostDTO((Post)obj);
            }
            if(obj instanceof User){
                return new UserDTO((User)obj);
            }
            if(obj instanceof Category){
                return new CategoryDTO((Category) obj);
            }
            return null;
        }).toList();
        return new SearchResultPageResponse(
                response,
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getTotalElements(),
                resultPage.getTotalPages(),
                resultPage.isLast()
        );
    }
}
