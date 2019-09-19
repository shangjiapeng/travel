package cn.itcast.travel.service;

import cn.itcast.travel.domain.User;

public interface UserService {
    /**
     * 用户注册操作
     *
     * @param user 封装用户注册数据
     * @return true:注册成功 false:注册失败
     */
    public boolean regist(User user);

    /**
     * 激活邮件
     *
     * @param code 激活码
     * @return
     */
   public boolean active(String code);

    /**
     * 用户登录功能
     *
     * @param user
     * @return
     */
    public User login(User user);
}
