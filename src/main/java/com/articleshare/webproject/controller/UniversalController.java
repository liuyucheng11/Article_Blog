package com.articleshare.webproject.controller;

import com.articleshare.webproject.Util.SessionUtil;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 前端控制器
 */
@Controller
public class UniversalController {

    @RequestMapping("/login")
    public String showLoginPage() {
        return "webapp/login";
    }

    @RequestMapping("/register")
    public String register() {
        return "webapp/register";
    }

    @RequestMapping("/home")
    public String showHomepage() {
        return "webapp/home";
    }

    @RequestMapping("/index")
    public String showIndexPage() {
        return "webapp/index";
    }

    @RequestMapping("/showArticleInfo")
    public String showArticleInfo() {
        return "webapp/showArticleInfo";
    }

    @RequestMapping("/readArticle")
    public String readArticle() {
        return "webapp/readArticle";
    }

    @RequestMapping("checkLogin.do")
    @ResponseBody
    public boolean checkLogin(){
        if(SessionUtil.getSessionAttribute("User")==null)
            return  false;
        else
            return true;
    }
}
