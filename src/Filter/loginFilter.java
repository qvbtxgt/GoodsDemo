package Filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class loginFilter
 */
@WebFilter("/loginFilter")
public class loginFilter implements Filter {
	private FilterConfig allow;
 
	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
		System.out.println("filter destroy!");
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		System.out.println("dofilter begin!");
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		HttpSession session = req.getSession();
		String str = allow.getInitParameter("allow");//初始化允许进入页面名称
		System.out.println(str);
		if(str != null){
			String[] strArray = str.split(",");
			for (int i = 0; i < strArray.length; i++) {
				if(strArray[i]==null || "".equals(strArray[i]))continue;
				//判断请求URL中是否包含允许进入页面，如果有，则跳转，如果没有则继续
				if(req.getRequestURI().indexOf(strArray[i])!=-1){
					chain.doFilter(request, response);
					return;
				}
			}
		}
		
		if(session.getAttribute("username")!=null){
			// pass the request along the filter chain
			chain.doFilter(request, response);
		}else{
			res.sendRedirect("login.jsp");
		}
		System.out.println("dofilter end!");
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
		System.out.println("init filter!");
		allow = fConfig;
	}

}
