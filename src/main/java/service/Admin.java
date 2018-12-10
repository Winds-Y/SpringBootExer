package service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.util.Cookie;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by: Changze
 * Date: 2018/12/10
 * Time: 18:58
 */
@Service
public class Admin {
    public void ChangePwd(String user_id,String new_pwd) throws IOException {
        WebClient webClient=new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setTimeout(0);
        webClient.getCookieManager().setCookiesEnabled(true);

        HtmlPage htmlPage= webClient.getPage("http://acm.wust.edu.cn/loginpage.php");
        HtmlForm htmlForm=htmlPage.getForms().get(0);
        HtmlElement button=htmlPage.getElementByName("submit");
        HtmlTextInput nameField= htmlForm.getInputByName("user_id");
        nameField.setValueAttribute("自己的账号");
        HtmlPasswordInput pwdField=htmlForm.getInputByName("password");
        pwdField.setValueAttribute("自己的密码");
        button.click();
        Set<Cookie> cookies=webClient.getCookieManager().getCookies();
        Map<String,String>responseCookies=new HashMap<>();
        for (Cookie c:cookies){
            responseCookies.put(c.getName(),c.getValue());
        }
        HtmlPage nextPage=webClient.getPage("保密");
        HtmlForm form1=nextPage.getForms().get(0);
        HtmlTextInput name=form1.getInputByName("user_id");
        HtmlTextInput pwd=form1.getInputByName("password");
        HtmlInput btn=form1.getInputsByValue("xxxx").get(0);
        name.setValueAttribute(user_id);
        pwd.setValueAttribute(new_pwd);
        String result=nextPage.asXml();
        btn.click();
        webClient.close();
    }
    public boolean checkExist(String id,String mail) throws IOException {
        WebClient webClient=new WebClient();
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setTimeout(0);
        webClient.getCookieManager().setCookiesEnabled(true);
        HtmlPage htmlPage=webClient.getPage("http://acm.wust.edu.cn/loginpage.php");
        HtmlForm form=htmlPage.getForms().get(0);
        HtmlElement button=htmlPage.getElementByName("submit");
        HtmlTextInput nameField = form.getInputByName("user_id");
        nameField.setValueAttribute("自己的账号");
        HtmlPasswordInput pwdField = form.getInputByName("password");
        pwdField.setValueAttribute("自己的密码");
        button.click();
        Set<Cookie> cookies = webClient.getCookieManager().getCookies();
        Map<String, String> responseCookies = new java.util.HashMap();
        for (Cookie c : cookies) {
            responseCookies.put(c.getName(), c.getValue());
        }
        HtmlPage nextPage = webClient.getPage("http://acm.wust.edu.cn/userinfo.php?user=" + id);
        if (nextPage.getByXPath("/html/body/div[@id='wrapper']/div[@id='main']/center/font[1]").size() == 0)//这里是xpath表达式获取页面元素
        {
            return false;
        }
        HtmlElement item = (HtmlElement)nextPage.getByXPath("/html/body/div[@id='wrapper']/div[@id='main']/center/font[1]").get(0);
        String xml = item.asXml();
        String mails = "";
        String regex = "[a-zA-z\\.[0-9]]*@[a-zA-z[0-9]]*\\.com";
        Pattern p=Pattern.compile(regex);
        Matcher m=p.matcher(xml);
        int num=0;
        while (m.find()){
            mails= m.group();
        }
        webClient.close();
        return mails.equals(mail);
    }
}
