package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.dao.impl.UserDaoImpl;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.util.MailUtils;
import cn.itcast.travel.util.UuidUtil;

public class UserServiceImpl implements UserService {
    private UserDao userDao = new UserDaoImpl();

    /**
     * 用户注册
     * @param user 封装用户注册数据
     * @return
     */
    @Override
    public boolean regist(User user) {
        //1 .根据用户名查询用户对象
        User u = userDao.findByUsername(user.getUsername());
        if (u!=null){
            //用户已经存在,注册失败
            return false;
        }
        //能够运行到这一步,说明能够注册成功
        //2 保存用户的信息
        //2.1设置激活码,唯一字符串
        user.setCode(UuidUtil.getUuid());
        //2.2设置激活状态
        user.setStatus("N");
        userDao.save(user);
        //3 发送激活邮件 邮件正文
        String content="<a href='travel/user/active?code="+user.getCode()+"'>点击激活【黑马旅游网】</a>";
        MailUtils.sendMail(user.getEmail(),content,"激活邮件");
        System.out.println("发送成功");
        return true;
    }

    /**
     * 用户激活
     * @param code 激活码
     * @return
     */
    @Override
    public boolean active(String code) {
        // 根据激活码查询用户对象
        int row =userDao.updateStatus(code,"Y");
        if (row>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @Override
    public User login(User user) {
        User u = userDao.findByUsernameAndPassword(user.getUsername(), user.getPassword());
        return u;
    }


}
