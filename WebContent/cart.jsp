<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.Set"%>
<%@page import="java.util.Map.Entry"%>
<%@page import="com.entity.Items"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.entity.Cart"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
 <link type="text/css" rel="stylesheet" href="css/style1.css" />
<script type="text/javascript">
	function delcfm() {
        if (!confirm("确认要删除？")) {
            window.event.returnValue = false;
        }else{
        	window.event.returnValue = true;
        }
    }
</script>
<title>购物车</title>
</head>
<body>
	<h1>我的购物车</h1>
   <a href="users.jsp">首页</a> >> <a href="users.jsp">商品列表</a>
   <hr> 
   <div id="shopping">
   <form action="" method="">		
			<table>
				<tr>
					<th>商品名称</th>
					<th>商品单价</th>
					<th>商品价格</th>
					<th>购买数量</th>
					<th>操作</th>
				</tr>
		<%
		Cart cart = (Cart)request.getSession().getAttribute("cart");
		if(cart != null){
			HashMap<Items,Integer> goods = cart.getGoods();
			Set<Map.Entry<Items,Integer>> entry = goods.entrySet();
			Iterator<Entry<Items,Integer>> it = entry.iterator();
			while(it.hasNext()){
				Items i = it.next().getKey();
		%>			
				<tr name="products" id="product_id_1">
					<td class="thumb"><img src="images/<%=i.getPicture()%>" /><a href=""><%=i.getName()%></a></td>
					<td class="number"><%=i.getPrice() %></td>
					<td class="price" id="price_id_1">
						<span><%=i.getPrice()*goods.get(i) %></span>
						<input type="hidden" value="" />
					</td>
					<td class="number">
                     	<%=goods.get(i)%>					
					</td>                        
                    <td class="delete">
					  <a href="CartServlet?action=delete&id=<%=i.getId()%>" onclick="delcfm();">删除</a>					                  
					</td>
				</tr>
		<%			
			}
		%>
			</table>
			<div class="total"><span id="total">总计：<%=cart.getTotalPrice() %>￥</span></div>
		<%
			}
		%>
		</form>
	</div>
</body>
</html>