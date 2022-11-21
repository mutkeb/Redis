package com.youkeda.app;

import com.youkeda.app.control.CommentController;
import com.youkeda.app.model.Comment;
import com.youkeda.app.model.Result;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AppApplicationTests {

    @Autowired
    private CommentController commentController;

    @Test
    void contextLoads() {
        Comment comment = new Comment();
        comment.setLikes(1l);
        Result<Long> result = commentController.add(comment);

        Assertions.assertThat(result.getData()).isEqualTo(1);
    }

}
