package cn.itcast.travel.web.servlet;

import cn.itcast.travel.service.UserService;
import cn.itcast.travel.service.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/activeUserServlet")
public class ActiveUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 获取激活码
        String code = request.getParameter("code");
        if (code != null) {
            //调用service 层进行激活操作
            UserService userService = new UserServiceImpl();
            boolean flag = userService.active(code);
            //3. 判断标记
            String msg = null;
            if (flag) {
                //激活成功
                msg = "激活成功,请点击<a href='http://localhost:8080/travel/login.html'>登录</a>";
            } else {
                //激活失败
                msg = "激活失败,当前线路繁忙,请稍后再试!";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
