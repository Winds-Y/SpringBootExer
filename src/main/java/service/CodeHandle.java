package service;

import com.zaxxer.hikari.util.FastList;
import entity.User;
import mapper.UserMapper;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.logging.log4j.message.ReusableMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.nio.cs.FastCharsetProvider;

import javax.xml.crypto.Data;
import java.util.Date;

/**
 * Created by: Changze
 * Date: 2018/12/10
 * Time: 18:58
 */
@Service
public class CodeHandle {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    @Transactional(rollbackFor =Exception.class )
    public boolean checkvalid(User user){
        User user1=userMapper.findUser(user.getId());
        return user.equals(user1);
    }
    @Autowired
    @Transactional(rollbackFor = Exception.class)
    public boolean CheackRepeat(User user){
        User user1=userMapper.findUser(user.getId());
        if(user1==null){
            return false;
        }
        Date d1=new Date();
        Date d2=  user1.getTime();
        if(d1.getTime()-d2.getTime()>=120000){
            userMapper.deleteUser(user.getId());
            return false;
        }
        return true;
    }

}
