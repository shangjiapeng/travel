package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.UserDao;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;


public class UserDaoImpl implements UserDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public User findByUsername(String username) {
        try {
            //定义sql
            String sql = "select *from tab_user where username=?";
            //执行sql
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class), username);
            return user;
        } catch (DataAccessException e) {
            return null;
        }
    }

    @Override
    public void save(User user) {
        //定义sql
        String sql = "insert into  tab_user(username,password,name,birthday,sex,telephone,email,status,code) values(?,?,?,?,?,?,?,?,?)";
        //执行sql
        template.update(sql, user.getUsername(),
                user.getPassword(),
                user.getName(),
                user.getBirthday(),
                user.getSex(),
                user.getTelephone(),
                user.getEmail(),
                user.getStatus(),
                user.getCode());
    }

    @Override
    public int updateStatus(String code, String status) {
        String sql = "update tab_user set status=? where code =? ";
        int row = template.update(sql, status, code);
        return row;
    }

    @Override
    public User findByUsernameAndPassword(String username, String password) {
        try {
            //定义sql
            String sql = "select *from tab_user where username=? and password=?";
            User user = template.queryForObject(sql, new BeanPropertyRowMapper<User>(User.class),username,password);
            return user;
        } catch (Exception e) {
            return null;
        }
    }
}
