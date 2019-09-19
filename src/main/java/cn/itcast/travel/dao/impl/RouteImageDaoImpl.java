package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteImageDao;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class RouteImageDaoImpl implements RouteImageDao{
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    /**
     * 根据rid查询所有的图片信息
     * @param rid
     * @return
     */
    @Override
    public List<RouteImg> findByRid(int rid) {
        String sql = "select * from  tab_route_img where rid = ?  ";
        List<RouteImg> imgList = template.query(sql, new BeanPropertyRowMapper<RouteImg>(RouteImg.class), rid);
        return imgList;
    }
}
