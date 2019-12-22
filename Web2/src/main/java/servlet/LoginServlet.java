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

public class LoginServlet extends HttpServlet {
    private final UserService userService;

    public LoginServlet() {
        this.userService = UserService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String pass =  req.getParameter("password");
        String email = req.getParameter("email");
        Map<String, Object> pageVariables = createPageVariablesMap(req);

        if ((pass == null || email == null)||(pass.equals("")||email.equals(""))) {
            pageVariables.put("message", "Please enter correct email/pass");
        } else {
            User user = new User(email, pass);

            if (userService.isExistsThisUser(user)){
                boolean userlExist = userService.isExistsThisUser(user);
                pageVariables.put("userlExist", userlExist);

                if (userService.authUser(user)) {
                    pageVariables.put("message", "Thank you for login");
                }
                else {
                    pageVariables.put("message", "You already login");
                }
            } else {
                pageVariables.put("message", "Please enter correct email/pass or use registration");
                pageVariables.put("userlExist", false);
            }

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

        List<User> loglist = userService.getAllAuth();
        StringBuilder logliststr = new StringBuilder();
        for (User user : loglist){
            logliststr.append("id: ")
                    .append(user.getId())
                    .append(" email: ")
                    .append(user.getEmail())
                    .append(" pass: ")
                    .append(user.getPassword())
                    .append("; ");
        }
        if (regliststr == null) regliststr.append("0");
        if (logliststr == null) logliststr.append("0");

        pageVariables.put("regList", regliststr);
        pageVariables.put("loglist", logliststr);

        resp.getWriter().println(PageGenerator.getInstance().getPage("authPage.html", pageVariables));
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
        pageVariables.put("email", email);
        pageVariables.put("password", pass);
        resp.getWriter().println(PageGenerator.getInstance().getPage("authPage.html", pageVariables));
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
        pageVariables.put("userlExist", request.getParameter("userlExist"));
        pageVariables.put("regList", "0 users");
        pageVariables.put("loglist", "0 users");
        pageVariables.put("userExist", false);
        return pageVariables;
    }
}
