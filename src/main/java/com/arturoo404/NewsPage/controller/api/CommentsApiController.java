package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.entity.comments.dto.CommentsDetailDto;
import com.arturoo404.NewsPage.exception.ExistInDatabaseException;
import com.arturoo404.NewsPage.service.CommentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@Controller
@RequestMapping("/api/comments")
public class CommentsApiController {

    private final CommentsService commentsService;

    public CommentsApiController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Object> addComments(@RequestBody AddCommentsDto addCommentsDto) throws ParseException {
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
}
