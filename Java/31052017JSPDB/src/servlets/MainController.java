package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by user on 31.05.2017.
 */
public class MainController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if(req.getParameter("PAGE_TYPE").equals("Items")) {
            req.setAttribute("items", new service.Service().getProducts());
            req.getRequestDispatcher("items.jsp").forward(req, resp);

        } else if(req.getParameter("PAGE_TYPE").equals("Products")) {
            req.setAttribute("descs", new service.Service().getDescriptions());
            req.getRequestDispatcher("products.jsp").forward(req, resp);

        }
    }
}
