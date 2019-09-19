package cn.itcast.travel.dao.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.util.JDBCUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@SuppressWarnings("all")
public class FavoriteDaoImpl implements FavoriteDao {
    JdbcTemplate template = new JdbcTemplate(JDBCUtils.getDataSource());

    @Override
    public Favorite findByRidAndUid(int rid, int uid) {
        Favorite favorite = null;
        try {
            String sql = "SELECT * FROM tab_favorite WHERE rid =? AND uid =? ";
            favorite = template.queryForObject(sql, new BeanPropertyRowMapper<Favorite>(Favorite.class), rid, uid);
        } catch (DataAccessException e) {
            System.out.println("该线路没有被用户收藏");
        }
        return favorite;
    }

    @Override
    public int findCountByRid(int rid) {
        String sql = "SELECT COUNT(*) FROM tab_favorite WHERE rid =?";
        Integer count = template.queryForObject(sql, Integer.class, rid);
        return count;
    }

    @Override
    public void addFavorite(int rid, int uid) {
        String sql = "INSERT INTO tab_favorite VALUES(?,?,?)";
        template.update(sql, rid, new Date(), uid);
    }

    @Override
    public List<Route> findListByUid(int uid, int start, int pageSize) {
        String sql = "SELECT * FROM tab_route WHERE rid IN (SELECT rid FROM tab_favorite WHERE uid = ? ) LIMIT ?,?";
        List<Route> routeList = template.query(sql, new BeanPropertyRowMapper<Route>(Route.class), uid, start, pageSize);
        return routeList;
    }

    @Override
    public int findCountByUid(int uid) {
        String sql = "select count(*) from tab_favorite where uid =?";
        Integer totalCount = template.queryForObject(sql, Integer.class, uid);
        return totalCount;
    }

    @Override
    public int findRankCount(String rname, int minPrice, int maxPrice) {
        //拼接sql语句 动态sql
        String sql = "SELECT COUNT(*) FROM  ( SELECT DISTINCT r.rid FROM tab_favorite f INNER JOIN tab_route r ON f.rid = r.rid where 1=1 ";
        StringBuilder sb = new StringBuilder(sql);

        //定义参数集合
        List<Object> params = new ArrayList<Object>();
        // 用户输入rname
        if (rname != null && rname.length() > 0 && !rname.equals("null")) {
            sb.append(" and r.rname like ? ");
            params.add("%" + rname + "%");
        }

        //用户输入最小价格
        if (minPrice > 0) {
            sb.append(" and r.price > ?  ");
            params.add(minPrice);
        }

        //用户输入最大价格
        if (maxPrice > 0) {
            sb.append(" and r.price < ?  ");
            params.add(maxPrice);
        }
        sb.append(" ) t  ");

        Integer count = template.queryForObject(sb.toString(), Integer.class, params.toArray());
        return count;
    }


    @Override
    public List<Route> findRankList(int start, int pageSize, String rname, int minPrice, int maxPrice) {
        //拼接sql语句 动态sql
        String sql = "SELECT COUNT(*) `count`,r.rid,r.rname,r.price,r.rimage,r.routeIntroduce FROM tab_favorite f INNER JOIN tab_route r ON f.rid = r.rid where 1=1    ";
        StringBuilder sb = new StringBuilder(sql);

        //定义参数集合
        List<Object> params = new ArrayList<Object>();
        // 用户输入rname
        if (rname != null && rname.length() > 0 && !rname.equals("null")) {
            sb.append(" and r.rname like ? ");
            params.add("%"+rname+"%");
        }

        //用户输入最小价格
        if(minPrice>0){
            sb.append(" and r.price > ?  ");
            params.add(minPrice);
        }

        //用户输入最大价格
        if(maxPrice>0){
            sb.append(" and r.price < ?  ");
            params.add(maxPrice);
        }

        sb.append(" GROUP BY f.rid  order by count desc limit  ?,?  ");
        params.add(start);
        params.add(pageSize);

        List<Route> list = template.query(sb.toString(), new BeanPropertyRowMapper<Route>(Route.class), params.toArray());
        return list;
    }
}
