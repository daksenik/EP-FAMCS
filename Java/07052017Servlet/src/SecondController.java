import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created by user on 17.05.2017.
 */
public class SecondController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession userSession = request.getSession();

        userSession.setAttribute(MyHttpConsts.NAME, request.getParameter(MyHttpConsts.NAME));
        userSession.setAttribute(MyHttpConsts.SURNAME, request.getParameter(MyHttpConsts.SURNAME));
        userSession.setAttribute(MyHttpConsts.AGE, request.getParameter(MyHttpConsts.AGE));

        request.getRequestDispatcher("third.jsp").forward(request, response);
    }
}
