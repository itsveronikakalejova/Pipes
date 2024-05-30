package sk.tuke.gamestudio.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import sk.tuke.gamestudio.entity.Comment;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommentServiceRestClient implements CommentService {
    private final String url = "http://localhost:8080/api/comments";

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void addComment(Comment comment) {
        restTemplate.postForEntity(url, comment, Comment.class);
    }

    @Override
    public List<Comment> getComments(String gameName) {
        ResponseEntity<Comment[]> commentListEntity = restTemplate.getForEntity(url + "/" + gameName, Comment[].class);
        if (commentListEntity.getBody() != null) {
            return Arrays.asList(commentListEntity.getBody());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void reset() throws CommentException {
        throw new UnsupportedOperationException("Not supported via web service");
    }
}
