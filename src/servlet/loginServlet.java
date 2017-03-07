package servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.setCharacterEncoding("utf-8");
		String username = URLEncoder.encode(request.getParameter("username"),"utf-8");
        //使用URLEncoder解决无法在Cookie当中保存中文字符串问题
        String password = URLEncoder.encode(request.getParameter("password"),"utf-8");
       
	       //首先判断用户是否选择了记住登录状态
	       //request.getParameterValues()返回的是一个字符串数组，实际“checkbox”被选中时返回“on”，否则返回“null”
	       String[] isUseCookies = request.getParameterValues("isUseCookie");
	      if(isUseCookies!=null&&isUseCookies.length>0)
	       {
	          //把用户名和密码保存在Cookie对象里面
	           
	          Cookie usernameCookie = new Cookie("username",username);
	          Cookie passwordCookie = new Cookie("password",password);
	          usernameCookie.setMaxAge(864000);
	          passwordCookie.setMaxAge(864000);//设置最大生存期限为10天
	          response.addCookie(usernameCookie);
	          response.addCookie(passwordCookie);
	       }
	       else
	       {
	          Cookie[] cookies = request.getCookies();
	          if(cookies!=null&&cookies.length>0)
	          {
	             for(Cookie c:cookies)
	             {
	                if(c.getName().equals("username")||c.getName().equals("password"))
	                {
	                    c.setMaxAge(0); //设置Cookie失效
	                    response.addCookie(c); //重新保存。
	                }
	             }
	          }
	       }
	      if("admin".equals(username) && "admin".equals(password)){
	    	  HttpSession session = request.getSession();
	    	  session.setAttribute("username", username);
	    	  response.sendRedirect(request.getContextPath()+"/users.jsp");
	      }else{
	    	  response.sendRedirect(request.getContextPath()+"/failure.jsp");
	      }
	      
	}

}
