package servlets;

import beans.ProductItem;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;

/**
 * Created by user on 31.05.2017.
 */
public class EditItem extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service.Service srv = new service.Service();
        ArrayList<ProductItem> pl = srv.getProducts();
        ProductItem result = pl.stream().filter(
                item -> item.getSidProduct() == Integer.parseInt(req.getParameter("sid"))).
                findFirst().get();
        if(req.getParameter("ACTION").equals("Edit")) {
            String date = req.getParameter("selling_date");
            if(date.length() != 0) result.setSellingDate(Date.valueOf(date));
            srv.updateItem(result);
        } else {
            srv.deleteProduct(result);
        }
        req.setAttribute("items", srv.getProducts());
        req.getRequestDispatcher("items.jsp").forward(req, resp);
    }
}
