package cn.itcast.travel.web.servlet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@WebServlet("/baseServlet")
public class BaseServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //完成方法的分发
        //1 获取请求的路径
        String uri = req.getRequestURI();

        //2 获取方法的名称
        String methodName = uri.substring(uri.lastIndexOf("/") + 1);

        //3 获取方法的对象Method     用到了反射的思想
        System.out.println(this); //cn.itcast.travel.controller.servlet.UserServlet@42d1f2bb
      //谁调用了service方法,this就代表谁.
        try {
            //getDeclaredMethod :忽略访问权限修饰符,获取方法,包括私有的方法都可以获取到
            Method method = this.getClass().getDeclaredMethod(methodName, HttpServletRequest.class, HttpServletResponse.class);
            //暴力反射
            //4 执行方法
            method.setAccessible(true);
            method.invoke(this,req,resp);

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 直接将传入的对象序列化为json 并且写回客户端
     * @param obj
     */
    public void writeValue(Object obj,HttpServletResponse response) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        response.setContentType("application/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),obj);
    }

    /**
     * 将传入的对象序列化为json 返回给调用者
     * @param obj
     */
    public String writeValueAsString(Object obj) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
       return mapper.writeValueAsString(obj);

    }
}
