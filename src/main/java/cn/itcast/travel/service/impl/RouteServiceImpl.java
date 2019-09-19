package cn.itcast.travel.service.impl;

import cn.itcast.travel.dao.FavoriteDao;
import cn.itcast.travel.dao.RouteDao;
import cn.itcast.travel.dao.RouteImageDao;
import cn.itcast.travel.dao.SellerDao;
import cn.itcast.travel.dao.impl.FavoriteDaoImpl;
import cn.itcast.travel.dao.impl.RouteDaoImpl;
import cn.itcast.travel.dao.impl.RouteImageDaoImpl;
import cn.itcast.travel.dao.impl.SellerDaoImpl;
import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.RouteImg;
import cn.itcast.travel.domain.Seller;
import cn.itcast.travel.service.RouteService;

import java.util.List;

public class RouteServiceImpl implements RouteService {
    private RouteDao routeDao = new RouteDaoImpl();
    private RouteImageDao routeImageDao = new RouteImageDaoImpl();
    private SellerDao sellerDao = new SellerDaoImpl();
    private FavoriteDao favoriteDao=new FavoriteDaoImpl();
    @Override
    public PageBean<Route> pageQuery(int cid, int currentPage, int pageSize, String rname) {
        //封装PageBean
        PageBean<Route> pageBean = new PageBean<Route>();
        //设置当前页码
        pageBean.setCurrentPage(currentPage);
        //设置每页显示的条数
        pageBean.setPageSize(pageSize);
        //设置总的记录数
        int totalCount = routeDao.findTotalCount(cid, rname);
        pageBean.setTotalCount(totalCount);
        //设置当前页显示的数据的集合
        int start = (currentPage - 1) * pageSize;
        List<Route> routeList = routeDao.findByPage(cid, start, pageSize, rname);
        pageBean.setList(routeList);
        //设置总页数
        int totalPage = (int) Math.ceil((double) totalCount / pageSize);//由于系统自动进行了类型转换
        System.out.println("totalPage:" + totalPage);
        pageBean.setTotalPage(totalPage);
        return pageBean;
    }

    @Override
    public Route findOne(String rid) {
        //1 根据rid去route表格中查询route对象
        Route route = routeDao.findOne(Integer.parseInt(rid));

        //2 根据 route 的rid 图片集合的集合信息
        List<RouteImg> imgList = routeImageDao.findByRid(route.getRid());
        //2.2 将图片集合设置到route对象
        route.setRouteImgList(imgList);

        //3 根据route的sid 查询商家对象
        Seller seller = sellerDao.findById(route.getSid());
        //3.2 将商家对象信息设置到route对象
        route.setSeller(seller);

        //4 查询收藏的次数
        int count=favoriteDao.findCountByRid(route.getRid());
        route.setCount(count);

        //5 返回数据
        return route;
    }
}
