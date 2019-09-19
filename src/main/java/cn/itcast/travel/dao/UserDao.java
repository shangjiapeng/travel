package cn.itcast.travel.dao;

import cn.itcast.travel.domain.User;

public interface UserDao {
    /**
     *
     * 根据用户名查询用户
     * @param username 用户名
     * @return 用户对象
     */
   public User findByUsername(String username);

    /**
     * 保存用户的信息到数据库
     * @param user
     */
    public void save(User user);

    /**
     * 更新用户的状态
     * @param code 需要更次状态的用户的 Uuid
     * @param status  激活状态 N 未激活 Y 激活
     * @return
     */
    public int updateStatus(String code, String status);

    /**
     * 根据用户名和密码查询用户
     * @param username
     * @param password
     * @return
     */
    public User findByUsernameAndPassword(String username, String password);
}
