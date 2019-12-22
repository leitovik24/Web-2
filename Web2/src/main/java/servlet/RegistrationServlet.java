package servlet;

import model.User;
import service.UserService;
import util.PageGenerator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegistrationServlet extends HttpServlet {
    private final UserService userService;

    public RegistrationServlet() {
        this.userService = UserService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pass =  req.getParameter("password");
        String email = req.getParameter("email");
        Map<String, Object> pageVariables = createPageVariablesMap(req);
        boolean emailExist = false;
        pageVariables.put("emailExist", emailExist);

        if ((pass == null || email == null)||(pass.equals("")||email.equals(""))) {
            pageVariables.put("message", "Bad data, please enter new");
        } else if(emailExist) {
            pageVariables.put("message", "This email is already exist, please enter new or login");
        }
        else {
            userService.addUser(new User(email, pass));
            pageVariables.put("message", "Thank you for registration");
        }

        List<User> reglist = userService.getAllUsers();
        StringBuilder regliststr = new StringBuilder();
        for (User user : reglist){
                regliststr.append("id: ")
                        .append(user.getId())
                        .append(" email: ")
                        .append(user.getEmail())
                        .append(" pass: ")
                        .append(user.getPassword())
                        .append("; ");
        }
        pageVariables.put("regList", regliststr);

        resp.getWriter().println(PageGenerator.getInstance().getPage("registerPage.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Map<String, Object> pageVariables = createPageVariablesMap(req);
        pageVariables.put("message", "Please, enter pass/email");
        String email = req.getParameter("email");
        if (email == null) {
            email = "please enter email";
        }
        String pass = req.getParameter("password");
        if (pass == null) {
            pass = "please enter pass";
        }

        pageVariables.put("emailExist", false);
        pageVariables.put("email", email);
        pageVariables.put("password", pass);
        resp.getWriter().println(PageGenerator.getInstance().getPage("registerPage.html", pageVariables));
        resp.setStatus(HttpServletResponse.SC_OK);
    }


    private static Map<String, Object> createPageVariablesMap(HttpServletRequest request) {
        Map<String, Object> pageVariables = new HashMap<>();
        String email = request.getParameter("email");
        String pass = request.getParameter("password");
        pageVariables.put("email", email);
        pageVariables.put("password", pass);
        pageVariables.put("method", request.getMethod());
        pageVariables.put("URL", request.getRequestURL().toString());
        pageVariables.put("emailExist", request.getParameter("emailExist"));
        pageVariables.put("regList", "0 users");

        return pageVariables;
    }
}
