package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by user on 01.06.2017.
 */
public class Login extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getParameter("login"),
                pass = req.getParameter("pass");
        
        int response = new service.Service().checkLogin(login, pass);
        
        if(response == -1) {
            req.getRequestDispatcher("index.jsp").forward(req, resp);
        } else {
            req.getSession().setAttribute("is_admin", response == 1 ? true : false);
            req.getSession().setAttribute("login", login);
            req.getRequestDispatcher("home.jsp").forward(req, resp);
        }
    }
}
