package io.clayicarus.blog.Controller;

import io.clayicarus.blog.DataBase.DB_User;
import io.clayicarus.blog.Mapper.UserMapper;
import org.apache.ibatis.annotations.Mapper;
import org.h2.engine.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {
    @GetMapping("/")
    String index(HttpServletRequest req)
    {
        // init page
        // use cookies to set session
        Cookie[] cookies = req.getCookies();    // FIXME: slow shit
        for(Cookie i : cookies) {
            if(i.getName().equals("token")) {
                String token = i.getValue();
                DB_User user = userMapper.getUserByToken(token);
                if(user != null) {
                    req.getSession().setAttribute("user", user);
                }
                break;
            }
        }
        return "index";
    }
    @Autowired
    private UserMapper userMapper;
}
