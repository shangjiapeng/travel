package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.ResultInfo;
import cn.itcast.travel.domain.User;
import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@WebServlet("/loginServlet")
public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
