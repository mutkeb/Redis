package com.youkeda.comment.api;

import com.youkeda.comment.model.Result;
import com.youkeda.comment.model.User;
import com.youkeda.comment.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author joe
 */
@Controller
public class UserAPI {
    private static final Logger LOG = LoggerFactory.getLogger(UserAPI.class);

    @Autowired
    private UserService userService;

    @PostMapping("/api/user/reg")
    public String reg(@RequestParam("userName") String userName, @RequestParam("pwd") String pwd, Model model) {
        Result<User> result = userService.register(userName, pwd);

        LOG.warn("register result : ", result);
        if (result != null && result.isSuccess()) {
            model.addAttribute("info", "注册成功！");
        } else {
            model.addAttribute("info", "注册失败！");
        }

        return "info.html";
    }

    @PostMapping("/api/user/login")
    public String login(@RequestParam("userName") String userName, @RequestParam("pwd") String pwd,
            HttpServletRequest request, Model model) {
        Result<User> result = userService.login(userName, pwd);

        LOG.warn("login result : ", result);
        if (result != null && result.isSuccess()) {
            request.getSession().setAttribute("userId", result.getData().getId());
            model.addAttribute("info", "登录成功！");
        } else {
            model.addAttribute("info", "登录失败！");
        }

        return "info.html";
    }

    @GetMapping("/api/user/logout")
    @ResponseBody
    public Result logout(HttpServletRequest request) {
        Result result = new Result();
        request.getSession().removeAttribute("userId");

        result.setSuccess(true);
        return result;
    }

}
