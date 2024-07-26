package org.prashant.blog.blogapplicationapi.controllers;

import lombok.RequiredArgsConstructor;
import org.prashant.blog.blogapplicationapi.payload.CreateTagRequest;
import org.prashant.blog.blogapplicationapi.payload.TagDT;
import org.prashant.blog.blogapplicationapi.payload.TagDto;
import org.prashant.blog.blogapplicationapi.payload.TagResponse;
import org.prashant.blog.blogapplicationapi.service.TagService;
import org.prashant.blog.blogapplicationapi.utils.AppConstant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/tag")
public class TagController {
    private final TagService tagService;

    @PostMapping("/create/")
    public ResponseEntity<TagDT> createTagHandler(@RequestBody CreateTagRequest createTagRequest){
        TagDT new_tag = tagService.createTag(createTagRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(new_tag);
    }

    @GetMapping("/")
    public ResponseEntity<TagResponse> getAllTagsHandler(
            @RequestParam(value ="pageNumber", defaultValue = AppConstant.PAGE_NUMBER, required = false) Integer pageNumber,
            @RequestParam(value ="pageSize", defaultValue = AppConstant.PAGE_SIZE, required = false) Integer pageSize,
            @RequestParam(value = "sortBy", defaultValue = AppConstant.DEFAULT_TAG_SORT_FIELD, required = false) String sortBy,
            @RequestParam(value = "sortDir", defaultValue = AppConstant.DEFAULT_SORT_CRITERIA, required = false) String sortDir

    ){
        TagResponse tagResponse = tagService.getAllTags(pageNumber, pageSize,sortBy, sortDir);
        return new ResponseEntity<>(tagResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{tagId}")
    public ResponseEntity<String> deleteTagHandler(@PathVariable Long tagId){
        tagService.deleteTag(tagId);
        return ResponseEntity.ok("tag deleted successfully with id "+tagId);
    }

    @DeleteMapping("/post/{postId}")
    public ResponseEntity<String> removeTagsByPostHandler(@PathVariable Long postId){
        tagService.removeTagsByPost(postId);
        return ResponseEntity.ok("tags deleted successfully with postId "+postId);
    }

}
