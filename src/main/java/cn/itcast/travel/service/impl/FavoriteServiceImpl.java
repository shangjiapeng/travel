package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.domain.Favorite;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.service.FavoriteService;

import java.util.List;

public class FavoriteServiceImpl implements FavoriteService {
    private FavoriteDao favoriteDao = new FavoriteDaoImpl();

    @Override
    public boolean isFavorite(String rid, int uid) {
        Favorite favorite = favoriteDao.findByRidAndUid(Integer.parseInt(rid), uid);
        if (favorite != null) {
            return true;  //如果能够查询到对象,返回true,
        } else {
            return false;
        }
    }

    @Override
    public void addFavorite(String rid, int uid) {
        favoriteDao.addFavorite(Integer.parseInt(rid), uid);
    }

    @Override
    public PageBean<Route> findFavoritePage(int uid, int currentPage, int pageSize) {
        //0.定义起始页码 -------------------->非常关键
        int start = (currentPage - 1) * pageSize;
        //1.根据uid查询所有当前页面所有数据
        List<Route> list = favoriteDao.findListByUid(uid, start, pageSize);
        //2.获取总记录数
        int totalCount = favoriteDao.findCountByUid(uid);
        //3.获取总页面数
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        //4.设置参数
        PageBean<Route> pb = new PageBean<>();
        pb.setTotalCount(totalCount);
        pb.setTotalPage(totalPage);
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        pb.setList(list);
        return pb;

    }

    @Override
    public PageBean<Route> findFavoriteRank(int currentPage, int pageSize, String rname, int minPrice, int maxPrice) {
        //1 定义起始页码
        int start = (currentPage - 1) * pageSize;
        //2 查询总的记录数
        int totalCount = favoriteDao.findRankCount(rname,minPrice,maxPrice );
        //3 查询收藏排行数据
        List<Route> list = favoriteDao.findRankList(start, pageSize, rname, minPrice,maxPrice);
        //4 计算总的页数
        int totalPage = (int) Math.ceil(totalCount * 1.0 / pageSize);
        //5 设置参数
        PageBean<Route> pb = new PageBean<>();
        pb.setTotalCount(totalCount);
        pb.setTotalPage(totalPage);
        pb.setCurrentPage(currentPage);
        pb.setPageSize(pageSize);
        pb.setList(list);
        return pb;
    }


}