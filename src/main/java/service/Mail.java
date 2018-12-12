package service;

import com.sun.mail.util.MailSSLSocketFactory;
import entity.User;
import mapper.UserMapper;
import org.jvnet.mimepull.MIMEMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.swing.*;
import java.util.Properties;
import java.util.Random;

/**
 * Created by: Changze
 * Date: 2018/12/10
 * Time: 18:58
 */
@Service
public class Mail {
    @Autowired
    UserMapper userMapper;
    @Transactional(rollbackFor = Exception.class)
    public void sendCode(User user){
        try {
            Properties prop=new Properties();
            prop.setProperty("mail.debug","true");
            prop.setProperty("mail.host","smtp.qq.com");
            prop.setProperty("mail.transport.protol","smtp");
            MailSSLSocketFactory sf=new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable","true");
            prop.put("mail.smtp.ssl.socketFactory",sf);
            Session session=Session.getInstance(prop);
            Transport ts=session.getTransport();
            ts.connect("smtp.qq.com","qq号","qq邮箱授权码");

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public MimeMessage createSimpleMail(User user,Session session) throws MessagingException {
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress("发件人邮箱"));
        message.setRecipients(Message.RecipientType.TO, String.valueOf(new InternetAddress(user.getMail())));
        message.setSubject("oj账号找回");
        Random rand=new Random();
        int t= rand.nextInt(90000) + 10000;
        user.setCode(Integer.toString(t));
        userMapper.insertUser(user);
        message.setContent("本次修改密码的验证码为："+t+"，2分钟内有效。若非本人操作，请联系管理员","text/html;charset=UTF-8");
        return message;
    }
}
