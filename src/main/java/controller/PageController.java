package controller;

import entity.User;
import mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import service.Admin;
import service.CodeHandle;
import service.Mail;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by: Changze
 * Date: 2018/12/10
 * Time: 18:57
 */
@Controller
@RequestMapping("/")
public class PageController {
    @Autowired
    Admin admin;
    @Autowired
    CodeHandle codeHandle;
    @Autowired
    Mail mail;
    @Autowired
    UserMapper userMapper;
    @GetMapping("/index")
    public String index(){
        return "index";
    }
    @PostMapping("/GetCode")
    @ResponseBody
    public Map<String,String>GetCode(HttpSession httpSession, User user){
        Map<String,String>map=new HashMap<>();
        String msg="位置错误";
        try {
            if(user.getId()==null||user.getMail()==null||("".equals(user.getId()))||("".equals(user.getMail()))){
                msg="账号或邮箱未填";
            }else if(admin.checkExist(user.getId(), user.getMail())){
                httpSession.setAttribute("uid",user.getId());
                httpSession.setAttribute("umail",user.getMail());
                if(codeHandle.CheackRepeat(user)){
                    msg="你已获得过验证码";
                }else {
                    mail.sendCode(user);
                    msg="获得验证码成功";
                }
            }else {
                msg="输入的账号或邮箱信息有误";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        map.put("msg",msg);
        return map;
    }

    @PostMapping("/CheckCode")
    @ResponseBody
    public Map<String,String>CheckCode(HttpSession httpSession, User user, @RequestParam("pwd") String pwd){
        Map<String,String>map=new HashMap<>();
        if(httpSession.getAttribute("uif")!=null){
            user.setId((String)httpSession.getAttribute("uid"));
        }
        if(httpSession.getAttribute("umail")!=null){
            user.setMail((String)httpSession.getAttribute("umail"));
        }
        if (user.getId()==null||user.getMail()==null||(user.getId().equals("")) || (user.getMail().equals(""))) {
            map.put("msg", "账号和邮箱不能为空!");
        }
        else if(user.getCode()==null||pwd==null||user.getCode().equals("")||pwd.equals(""))
        {
            map.put("msg","验证码或密码不能为空");
        }
        else if(codeHandle.CheackRepeat(user)){
            if(codeHandle.checkvalid(user)){
                try {
                    admin.ChangePwd(user.getId(),pwd);
                    map.put("msg","修改密码成功");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                userMapper.deleteUser(user.getId());
            }
            else {
                map.put("msg","输入验证码有误");
            }
        }else {
            map.put("msg","验证码过期或没有获得过验证码");
        }
        return map;
    }
}
