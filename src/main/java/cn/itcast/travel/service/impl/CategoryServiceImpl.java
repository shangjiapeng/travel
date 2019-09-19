package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.CategoryDao;
import cn.itcast.travel.dao.impl.CategoryDaoImpl;
import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.util.JedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();

    /**
     * 查询所有的Category
     *
     * @return
     */
    @Override
    public List<Category> findAll() {
        //1 从Redis中查询 ,获取jedis客户端
        Jedis jedis = JedisUtil.getJedis();
        //Set<String> categories = jedis.zrange("category", 0, -1);//range() 方法获取的是 category  的 cname 并没有获得cid
        //....................................

        //1.2 查询sortedset 中的分数(cid)和值(cname)
        Set<Tuple> categories = jedis.zrangeWithScores("category", 0, -1);
        //......................................

        List<Category> cs=null;
        //判断查询的结果是否为空,
        if (categories == null || categories.size() == 0) {
            System.out.println("从数据库中查询...");
            //如果为空的话说明是第一次查询,没有缓存
            //2 从数据库中查询
            cs = categoryDao.findAll();
            //将集合的数据存贮到Redis中
            for (int i = 0; i < cs.size(); i++) {
                jedis.zadd("category", cs.get(i).getCid(), cs.get(i).getCname());
            }
        } else {
            //如果不为空,则说明缓存中有数据 ,直接使用缓存中的数据即可
            System.out.println("从Redis中查询...");
            //然后要进行类型的转换 ,将set数据存到list中
            cs = new ArrayList<Category>();
            for (Tuple tuple : categories) {  //tuple
                Category category = new Category();
                category.setCname(tuple.getElement());
                category.setCid((int) tuple.getScore());
                cs.add(category);
            }
        }
        return cs;
    }
}
