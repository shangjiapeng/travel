package cn.itcast.travel.dao;

import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.Route;

import java.util.List;

public interface FavoriteDao {
    /**
     * 根据路线的rid和用户的uid 查询收藏信息
     * @param rid
     * @param uid
     * @return
     */
     public Favorite findByRidAndUid(int rid, int uid);

    /**
     * 根据rid 查询收藏的次数
     * @param rid
     * @return
     */
    public int findCountByRid(int rid);

    /**
     * 添加收藏的方法
     * @param rid
     * @param uid
     */
    public void addFavorite(int rid, int uid);


    /**
     * 根据uid查询所有的单收藏route信息
     * @param uid
     * @param start
     * @param pageSize
     * @return
     */
    public List<Route> findListByUid(int uid, int start, int pageSize);

    /**
     * 根据uid查询总的收藏记录数
     * @param uid
     * @return
     */
    public int findCountByUid(int uid);

    /**
     * 查询收藏排行榜总记录数
     * @param rname
     * @param minPrice
     * @param maxPrice
     * @return
     */
    int findRankCount(String rname, int minPrice, int maxPrice);

    /**
     * 查询收藏排行榜需要展示的所有的路线信息
     * @param start
     * @param pageSize
     * @param rname
     * @param minPrice
     * @param maxPrice
     * @return
     */
    List<Route> findRankList(int start, int pageSize, String rname, int minPrice, int maxPrice);
}
