package ru.uruydas.news.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.uruydas.comments.model.Comment;
import ru.uruydas.comments.service.CommentService;
import ru.uruydas.common.web.WebCommons;
import ru.uruydas.common.web.exception.ValidationException;
import ru.uruydas.likes.model.LikeResult;
import ru.uruydas.news.model.News;
import ru.uruydas.news.service.NewsService;
import ru.uruydas.news.web.model.CreateCommentRequest;
import ru.uruydas.news.web.model.CreateNewsRequest;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(WebCommons.VERSION_PREFIX + "/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

    @Autowired
    private CommentService commentService;

    @RequestMapping(method = RequestMethod.GET)
    public List<News> getLatestFromAllNews() {
        return newsService.getLatestAll(WebCommons.PAGING_COUNT);
    }

    @RequestMapping(method = RequestMethod.GET, params = "id")
    public List<News> getAllFromId(@RequestParam("id") Long id) {
        return newsService.getAllFromId(id, WebCommons.PAGING_COUNT);
    }

    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<String> createNewNews(@Valid @RequestBody CreateNewsRequest request,
                                                BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        newsService.create(request.toNews());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @RequestMapping(value = "/like/{id}", method = RequestMethod.POST)
    public LikeResult likeNews(@PathVariable("id") Long newsId) {
        return newsService.like(newsId);
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.GET)
    public List<Comment> getAllComments(@PathVariable("id") Long newsId) {
        return commentService.getAll(newsId);
    }

    @RequestMapping(value = "/{id}/comments", method = RequestMethod.POST)
    public List<Comment> createComment(@PathVariable("id") Long newsId,
                                       @Valid @RequestBody CreateCommentRequest request,
                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(bindingResult);
        }

        Comment comment = Comment.fromComment(request.toComment())
                .withNewsId(newsId)
                .build();

        commentService.create(comment);

        return commentService.getAll(newsId);
    }
}
