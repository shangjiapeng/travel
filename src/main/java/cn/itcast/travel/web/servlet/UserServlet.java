package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
@SuppressWarnings("all")
@WebServlet("/user/*")
public class UserServlet extends BaseServlet {
    /**
     * 注册功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void regist(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 校验 验证码
        String check = request.getParameter("check");
        // 从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//为了保证验证码只能使用一次
        //比较用户输入的验证码和session中保存的验证码
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证码错误
            ResultInfo info = new ResultInfo();
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("验证码不正确");
            //将info序列化为json
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(info);
            // 将json数据写回客户端
            response.getWriter().write(json);
            return;
        }
        //2 验证码校验通过之后继续完成注册操作
        //获取数据
        Map<String, String[]> map = request.getParameterMap();
        //使用BeanUtils 工具类封装对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        //3 调用service层 完成注册信息的存储
        UserService userService = new UserServiceImpl();
        boolean flag = userService.regist(user);
        ResultInfo info = new ResultInfo();
        if (flag) {
            //注册成功
            info.setFlag(true);
        } else {
            //注册失败
            info.setFlag(false);
            info.setErrorMsg("该用户名已经被占用,注册失败");
        }
        //3 将info 对象序列化为json
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(info);
        //4. 将json数据写回客户端
        response.getWriter().write(json);

    }

    /**
     * 激活功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void active(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            //2调用service 层进行激活操作
            UserService userService = new UserServiceImpl();
            boolean flag = userService.active(code);
            //3. 判断标记
            String msg = null;
            if (flag) {
                //激活成功
                msg = "激活成功,请点击<a href='travel/login.html'>登录</a>";
            } else {
                //激活失败
                msg = "激活失败,当前线路繁忙,请稍后再试!";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    /**
     * 登录功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResultInfo info = new ResultInfo();
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        //1 验证码校验
        String check = request.getParameter("check");
        //从session中获取验证码
        HttpSession session = request.getSession();
        String checkcode_server = (String) session.getAttribute("CHECKCODE_SERVER");
        session.removeAttribute("CHECKCODE_SERVER");//保证验证码只能够使用一次
        //比较验证码,验证失败,
        if (checkcode_server == null || !checkcode_server.equalsIgnoreCase(check)) {
            //验证失败,保存错误信息
            info.setFlag(false);
            info.setErrorMsg("验证码错误");
            //将错误信息序列化为json 并且响应到页面展示
            mapper.writeValue(response.getOutputStream(), info);
            return;
        }

        //2验证码验证通过后,继续进行用户登陆的操作
        //2.1获取用户名和密码
        Map<String, String[]> map = request.getParameterMap();
        //2.2封装user对象
        User user = new User();
        try {
            BeanUtils.populate(user, map);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //2.3  调用Service层进行查询操作
        UserService userService = new UserServiceImpl();
        User u = userService.login(user);

        //2.4 首先要判断用户的信息是否为空
        if (u == null) {
            //用户名或者密码错误
            info.setFlag(false);
            info.setErrorMsg("用户名或者密码错误");
            //将info对象序列化为json
            mapper.writeValue(response.getOutputStream(), info);
            return;
        }

        //3 判断用户是否激活
        if (u != null && !"Y".equals(u.getStatus())) {
            info.setFlag(false);
            info.setErrorMsg("您尚未激活,请先激活在登录");
            //将info 对象序列化为json
            mapper.writeValue(response.getOutputStream(), info);
            return;
        }

        //4判断登录是否成功
        if (u != null && "Y".equals(u.getStatus())) {
            request.getSession().setAttribute("user", u);//登录成功标记
            //登录成功
            info.setFlag(true);
            mapper.writeValue(response.getOutputStream(), info);
            return;
        }
    }

    /**
     * 查询单个对象
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findOne(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //从session中获取登录用户的信息
        Object user = request.getSession().getAttribute("user");
        //将user写到客户端
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(), user);
    }

    /**
     * 退出功能
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void exit(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 销毁session
        request.getSession().invalidate();
        //2 从新跳转到登录的界面
        response.sendRedirect(request.getContextPath() + "/login.html");
    }



}