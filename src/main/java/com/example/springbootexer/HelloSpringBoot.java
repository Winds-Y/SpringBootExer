package com.example.springbootexer;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by: Changze
 * Date: 2018/12/9
 * Time: 16:42
 */
@Controller
public class HelloSpringBoot {
    @RequestMapping("/index")
    public String index(){
        return "hello";
    }
}
