package webpack;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by user on 25.05.2017.
 */
public class MainPage extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if(request.getParameter("PAGE_TYPE").equals("Deals")) {
            request.setAttribute("result", database.DBAccess.getDeals());
            request.getRequestDispatcher("deals.jsp").forward(request, response);
            
        } else if(request.getParameter("PAGE_TYPE").equals("Products")) {
            request.setAttribute("result", database.DBAccess.getProducts());
            request.getRequestDispatcher("products.jsp").forward(request, response);
            
        } else if(request.getParameter("PAGE_TYPE").equals("Students")) {
            request.setAttribute("result", database.DBAccess.getStudents());
            request.getRequestDispatcher("students.jsp").forward(request, response);
        }
    }
}
