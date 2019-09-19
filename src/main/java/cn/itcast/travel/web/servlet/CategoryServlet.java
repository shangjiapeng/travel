package cn.itcast.travel.web.servlet;

import cn.itcast.travel.domain.Category;
import cn.itcast.travel.service.CategoryService;
import cn.itcast.travel.service.impl.CategoryServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet("/category/*")
public class CategoryServlet extends BaseServlet {
    private CategoryService service = new CategoryServiceImpl();

    /**
     * 查询所有
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void findAll(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //1 调用service 查询所有Category
        List<Category> categories = service.findAll();
        //2 序列化json 返回
        /*ObjectMapper mapper = new ObjectMapper();
        response.setContentType("applicatoin/json;charset=utf-8");
        mapper.writeValue(response.getOutputStream(),categories);*/
        writeValue(categories, response);  //直接 调用父类BaseServlet 中抽取出来的序列化json 的方法
    }

    protected void find(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("CategoryServlet 的find方法...");
    }
}
