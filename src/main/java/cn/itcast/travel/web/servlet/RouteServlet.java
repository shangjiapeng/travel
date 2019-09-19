package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.PageBean;
import cn.itcast.travel.domain.Route;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.FavoriteService;
import cn.itcast.travel.service.RouteService;
import cn.itcast.travel.service.impl.FavoriteServiceImpl;
import cn.itcast.travel.service.impl.RouteServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/route/*")
public class RouteServlet extends BaseServlet {
    private RouteService routeService = new RouteServiceImpl();
    private FavoriteService favoriteService = new FavoriteServiceImpl();

    /**
     * 分页查询 方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void pageQuery(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String cidStr = request.getParameter("cid");
        //接收rname 路线名称
        String rname = request.getParameter("rname");
        // rname = new String(rname.getBytes("iso-8859-1"),"utf-8");

        //2 处理参数 进行类型的转换
        //2.1处理cid
        int cid = 0;
        if (cidStr != null && cidStr.length() > 0 && !"null".equals(cidStr)) {
            cid = Integer.parseInt(cidStr);
        }
        //2.2 处理 currentPage  如果没有传递,则默认为第一页
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        System.out.println("currentPage-->" + currentPage);
        //2.3 处理 pageSize  如果没有传递则默认每页显示5条数据
        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 6;
        }
        //3 调用service 层查询pageBean 对象
        PageBean<Route> pageBean = routeService.pageQuery(cid, currentPage, pageSize, rname);
        //4 将pageBean 对象序列化为json 返回给客户端页面
        writeValue(pageBean, response);
    }

    /**
     * 根据rid 查询一个旅游线路的信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 接收从前台传来的rid
        String rid = request.getParameter("rid");
        //2 调用service 层方法查询route对象
        Route route = routeService.findOne(rid);
        //3 将查询的到信息写会客户端
        writeValue(route, response);
    }

    /**
     * 判断是否已经被收藏
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void isFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 从前台获取rid
        String rid = request.getParameter("rid");
        //2 获取当前session中存储的登录的user信息
        User user = (User) request.getSession().getAttribute("user");
        //设置用户的uid 信息
        int uid = 0;
        if (user == null) {//用户没有登录
            uid = 0; //赋值为0
        } else {
            uid = user.getUid();
        }
        //3 调用service 根据用户的uid 和前端传过来的rid查询数据库  返回一个flag
        boolean flag = favoriteService.isFavorite(rid, uid);
        //4 将数据写会客户端
        writeValue(flag, response);
    }

    /**
     * 添加收藏信息的方法
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void addFavorite(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 从前台获取参数 rid
        String rid = request.getParameter("rid");
        //2 获取当前session中存储的登录的user信息
        User user = (User) request.getSession().getAttribute("user");
        //设置用户的uid 信息
        int uid = 0;
        if (user == null) {//用户没有登录
            return;
        } else {
            uid = user.getUid();
        }
        //3 调用service 执行添加收藏的方法
        favoriteService.addFavorite(rid, uid);
    }

    /**
     * 根据uid查询用户收藏数据信息
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void favoritePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 接收参数
        String uidStr = request.getParameter("uid");
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        //2 处理参数 进行类型的转换
        //2.1处理uid
        //设置用户的uid 信息
        int uid = 0;
        if (uidStr != null && uidStr.length() > 0 && !"null".equals(uidStr)) {
            uid = Integer.parseInt(uidStr);
        }
        //2.2 处理 currentPage  如果没有传递,则默认为第一页
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        System.out.println("currentPage-->" + currentPage);
        //2.3 处理 pageSize  如果没有传递则默认每页显示5条数据
        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 8;
        }

        //3 调用service 层查询pageBean 对象
        PageBean<Route> pageBean = favoriteService.findFavoritePage(uid, currentPage, pageSize);
        //4 将pageBean 对象序列化为json 返回给客户端页面
        writeValue(pageBean, response);
    }

    /**
     * 查询线路收藏排行数据
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    public void favoriteRankPage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 接收参数
        String currentPageStr = request.getParameter("currentPage");
        String pageSizeStr = request.getParameter("pageSize");
        String rname = request.getParameter("rname");
        String minPriceStr = request.getParameter("minPrice");
        String maxPriceStr = request.getParameter("maxPrice");
        //2 处理参数 进行类型的转换
        //2.1 处理 currentPage  如果没有传递,则默认为第一页
        int currentPage = 0;
        if (currentPageStr != null && currentPageStr.length() > 0) {
            currentPage = Integer.parseInt(currentPageStr);
        } else {
            currentPage = 1;
        }
        System.out.println("currentPage-->" + currentPage);
        //2.2 处理 pageSize  如果没有传递则默认每页显示5条数据
        int pageSize = 0;
        if (pageSizeStr != null && pageSizeStr.length() > 0) {
            pageSize = Integer.parseInt(pageSizeStr);
        } else {
            pageSize = 8;
        }
        //2.3 处理mimPrice
        int minPrice = 0;
        if (minPriceStr != null && minPriceStr.length() > 0) {
            minPrice = Integer.parseInt(minPriceStr);
        }
        //2.4 处理mimPrice
        int maxPrice = 0;
        if (maxPriceStr != null && maxPriceStr.length() > 0) {
            maxPrice = Integer.parseInt(maxPriceStr);
        }
        //3 调用service 层查询pageBean 对象
        PageBean<Route> pageBean = favoriteService.findFavoriteRank(currentPage, pageSize, rname, minPrice, maxPrice);
        //4 将pageBean 对象序列化为json 返回给客户端页面
        writeValue(pageBean, response);
    }
}