package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.domain.Route;

import cn.itcast.travel.util.JDBCUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class RouteDaoImpl implements RouteDao {
    private JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public int findTotalCount(int cid, String rname) {
        //String sql = "SELECT COUNT(*) FROM  tab_route WHERE cid = ?";
        // 1 定义sql模板
        String sql = "SELECT COUNT(*) FROM  tab_route where 1=1  ";
        StringBuilder sb = new StringBuilder(sql);
        List<Object> params = new ArrayList<Object>();//条件集合
        // 2 判断参数是否有值
        if (cid != 0){
            sb.append(" and cid = ? ");
            params.add(cid);
        }
        if(rname!=null &&rname.length()>0 &&!rname.equals("null")){  //注意这里的条件的判断呢 还要加上空字符串的判断
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        sql=sb.toString();
        //params.toArray() :作用是,方便串可变的参数,因为参数的个数不固定的,不能直接在queryForObject() 方法中直接写参数的名称
            Integer totalCount = template.queryForObject(sql, Integer.class, params.toArray());
        return totalCount;
    }


    @Override
    public List<Route> findByPage(int cid, int start, int pageSize, String rname) {
        //String sql = "SELECT * FROM tab_route WHERE cid = ? limit ? ,? ";
        // 1 定义sql模板
        String sql = "SELECT * FROM tab_route WHERE 1=1  ";
        StringBuilder sb = new StringBuilder(sql);
        List<Object> params = new ArrayList<Object>();//条件集合
        // 2 判断参数是否有值
        if (cid != 0){
            sb.append(" and cid = ? ");
            params.add(cid);
        }
        if(rname!=null &&rname.length()>0 && !rname.equals("null")){
            sb.append(" and rname like ? ");
            params.add("%"+rname+"%");
        }
        //再添加到分页的限制条件
        sb.append(" limit ?,? ");
        params.add(start);
        params.add(pageSize);
        sql=sb.toString();
        List<Route> routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
        return routeList;
    }


    @Override
    public Route findOne(int rid) {
        String sql ="select * from  tab_route where rid= ? ";
        Route route = template.queryForObject(sql, new BeanPropertyRowMapper<Route>(Route.class), rid);
        return route;
    }
}
