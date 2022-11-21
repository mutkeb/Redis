package com.youkeda.app.control;

import com.youkeda.app.model.Comment;
import com.youkeda.app.model.Result;
import com.youkeda.app.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @date 2020/6/16, 周二
 */
@Controller
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @PostMapping("/add")
    public Result<Long> add(@RequestBody Comment comment) {

        Result<Long> result = new Result<>();

        //初始化点赞数
        comment.setLikes(0l);

        result.setData(commentService.add(comment));
        result.setSuccess(true);

        return result;
    }

}
