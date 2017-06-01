package servlets;

import beans.ProductDesc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by user on 01.06.2017.
 */
public class EditProducts extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        service.Service srv = new service.Service();
        ArrayList<ProductDesc> pl = srv.getDescriptions();
        int percentage = Integer.parseInt(req.getParameter("delta").toString());
        if(req.getParameter("id").toString().length() > 0){
            ProductDesc result = pl.stream().filter(
                    desc -> desc.getIdProduct() == Integer.parseInt(req.getParameter("id"))).
                    findFirst().get();
            result.setPrice(result.getPrice() * percentage / 100);
            srv.updateProduct(result);
        } else {
            srv.updatePrices(percentage);
        }
        
        req.setAttribute("descs", srv.getDescriptions());
        req.getRequestDispatcher("products.jsp").forward(req, resp);
    }
}
