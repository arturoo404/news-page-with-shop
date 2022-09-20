package com.arturoo404.NewsPage.controller.api.news;

import com.arturoo404.NewsPage.entity.news.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.entity.news.comments.dto.CommentsDetailDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.news.CommentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/comments")
public class CommentsApiController {

    private final CommentsService commentsService;

    @Autowired
    public CommentsApiController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping(path = "/add")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Object> addComments(@RequestBody AddCommentsDto addCommentsDto) {
        try {
            commentsService.addComments(addCommentsDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Successfully created comments.");
    }

    @GetMapping(path = "/list")
    public ResponseEntity<List<CommentsDetailDto>> commentsList(@RequestParam("articleId") Long articleId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(commentsService.getCommentsDetail(articleId));
    }

    //TODO Comments delete test
    @PreAuthorize("hasAnyAuthority('ADMIN', 'MODERATOR')")
    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteComments(@PathVariable("id") Long id){
        try {
            commentsService.deleteComments(id);
        } catch (ExistInDatabaseException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body("Comments has been deleted.");
    }
}
