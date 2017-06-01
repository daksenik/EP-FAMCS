package servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by user on 01.06.2017.
 */
public class EditProfile extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String login = req.getSession().getAttribute("login").toString();
        boolean isAdmin = Boolean.parseBoolean(req.getSession().getAttribute("is_admin").toString());
        String newPassword = req.getParameter("password");
        String target = req.getParameter("editable_user");
        
        service.Service srv = new service.Service();
        if(target == null || target.length() == 0) {
            srv.updatePassword(login, newPassword);
        } else if(isAdmin) {
            srv.updatePassword(target, newPassword);
        }

        req.getRequestDispatcher("home.jsp").forward(req, resp);
    }
}
