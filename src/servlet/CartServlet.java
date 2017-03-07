package servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DBItems;
import com.entity.Cart;
import com.entity.Items;

/**
 * Servlet implementation class CartServlet
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private String action ;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("GET method");
		response.setContentType("text/html;charset=utf-8");
		if (request.getParameter("action")!=null) {
			this.action = request.getParameter("action");
			if (action.equals("add")) {
				if(addToCart(request,response)){
					request.getRequestDispatcher("/success.jsp").forward(request, response);
				}else{
					request.getRequestDispatcher("/failure.jsp").forward(request, response);
				}
			}
			if (action.equals("show")) {
				request.getRequestDispatcher("/cart.jsp").forward(request, response);
			}
			if(action.equals("delete")){
				if(deleteFromCart(request,response)){
					request.getRequestDispatcher("/cart.jsp").forward(request, response);
				}
			}
		}
		
	}

	//添加商品到购物车
	private boolean addToCart(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		int id = Integer.parseInt(request.getParameter("id"));
		int number = Integer.parseInt(request.getParameter("num"));
		Items item = DBItems.getItemByID(id);
		//判断session是否存在“购物车”属性，如不存在，则创建一个，如果存在，则在先获取再进行修改
		if (request.getSession().getAttribute("cart")==null) {
			Cart cart = new Cart();
			request.getSession().setAttribute("cart", cart);//将购物车放入session中
		}
		Cart cart = (Cart) request.getSession().getAttribute("cart");//从session中获取购物车
		if (cart.addGoods(item, number)) {
			return true;
		}else{
			return false;
		}
	}
	
	//从购物车中删除商品
	public boolean deleteFromCart(HttpServletRequest request, HttpServletResponse response){
		int id = Integer.parseInt(request.getParameter("id"));
		Items item = DBItems.getItemByID(id);
		Cart cart = (Cart) request.getSession().getAttribute("cart");//从session中获取购物车
		if(cart.deleteGoods(item)){
			return true;
		}else{
			return false;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		System.out.println("POST method");
		doGet(request, response);
	}
	
	public static void main(String[] args) {
		System.out.println("initial");
	}

}
