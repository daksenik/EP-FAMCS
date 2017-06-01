import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by user on 17.05.2017.
 */
public class FirstController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession userSession = request.getSession();

        userSession.setAttribute(MyHttpConsts.LOGIN, request.getParameter(MyHttpConsts.LOGIN));
        userSession.setAttribute(MyHttpConsts.EMAIL, request.getParameter(MyHttpConsts.EMAIL));

        request.getRequestDispatcher("second.jsp").forward(request, response);
    }
}
