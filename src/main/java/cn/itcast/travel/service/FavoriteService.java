package cn.itcast.travel.service;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;

public interface FavoriteService {
    /**
     * 判断是否是用户收藏的方法
     * @param rid
     * @param uid
     * @return
     */
    public boolean isFavorite(String rid,int uid);

    /**
     * 添加收藏
     * @param rid
     * @param uid
     */
    public void addFavorite(String rid, int uid);

    /**
     * 查询收藏信息
     * @param uid
     * @param currentPage
     * @param pageSize
     * @return
     */
    public PageBean<Route> findFavoritePage(int uid, int currentPage, int pageSize);

    /**
     * 查询收藏排行榜
     * @param currentPage
     * @param pageSize
     * @param rname
     * @param minPrice
     * @param maxPrice
     * @return
     */
    PageBean<Route> findFavoriteRank(int currentPage, int pageSize, String rname, int minPrice, int maxPrice);

}
