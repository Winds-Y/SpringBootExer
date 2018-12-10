package com.example.springbootexer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by: Changze
 * Date: 2018/12/9
 * Time: 16:42
 */
@Controller
public class HelloSpringBoot {
    @RequestMapping("/")
    public String index(ModelMap map){
        map.addAttribute("host","http://blog.didispace.com");
        return "hello";
    }
}
