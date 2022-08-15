package com.arturoo404.NewsPage.controller.api;

import com.arturoo404.NewsPage.entity.comments.dto.AddCommentsDto;
import com.arturoo404.NewsPage.service.CommentsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.text.ParseException;

@Controller
@RequestMapping("/api/comments")
public class CommentsApiController {

    private final CommentsService commentsService;

    public CommentsApiController(CommentsService commentsService) {
        this.commentsService = commentsService;
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Object> addComments(@RequestBody AddCommentsDto addCommentsDto) throws ParseException {
        commentsService.addComments(addCommentsDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Successfully created comments.");
    }
}
