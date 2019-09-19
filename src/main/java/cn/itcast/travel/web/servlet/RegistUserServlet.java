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

@WebServlet("/registUserServlet")
public class RegistUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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
        UserService userService= new UserServiceImpl();
        boolean flag=userService.regist(user);
        ResultInfo info = new ResultInfo();
        if (flag){
            //注册成功
            info.setFlag(true);
        }else {
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

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        this.doPost(request, response);
    }
}
