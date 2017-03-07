<%@page import="java.util.ArrayList"%>
<%@page import="com.dao.DBItems"%>
<%@page import="com.entity.Items"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>商品详细信息</title>

	<script type="text/javascript" src="js/lhgcore.js"></script>
    <script type="text/javascript" src="js/lhgdialog.js"></script>
<script type="text/javascript">
function selflog_show(id)
{ 
   var num =  document.getElementById("number").value; 
   //alert('hehe');
   //注意跳转路径 ，GET请求
    J.dialog.get({id: 'haoyue_creat',title: '购物成功',width: 600,height:400, link: '<%=path%>/CartServlet?id='+id+'&num='+num+'&action=add', cover:true});
}
function add(){
	var num = parseInt(document.getElementById("number").value);
	if(num<100){
		document.getElementById("number").value = ++num;
	}
}
function sub(){
	var num = parseInt(document.getElementById("number").value);
	if(num>1){
		document.getElementById("number").value = --num;
	}
}
</script>
<style type="text/css">
div {
	float: left;
	margin-left: 30px;
	margin-right: 30px;
	margin-top: 5px;
	margin-bottom: 5px;
}

div dd {
	margin: 0px;
	font-size: 10pt;
}

div dd.dd_name {
	color: blue;
}

div dd.dd_city {
	color: #000;
}
div #cart
	   {
	     margin:0px auto;
	     text-align:right; 
	   }
	   span{
	     padding:0 2px;border:1px #c0c0c0 solid;cursor:pointer;
	   }
	   a{
	      text-decoration: none; 
	   }
</style>
</head>
<body>
	<h1>商品详情</h1>
	<hr>
	<center>
		<table width="750" height="60" cellpadding="0" cellspacing="0"
			border="0">
			<tr>
				<!-- 显示商品详情 -->
				<%
				DBItems items = new DBItems();
				Items item = DBItems.getItemByID(Integer.parseInt(request.getParameter("id")));
				if (item != null) {
			%>
				<td width="70%" valign="top">
					<table>
						<tr>
							<td rowspan="5"><img src="images/<%=item.getPicture()%>"
								width="200" height="160" /></td>
						</tr>
						<tr>
							<td><B><%=item.getName()%></B></td>
						</tr>
						<tr>
							<td>产地：<%=item.getCity()%></td>
						</tr>
						<tr>
							<td>价格：<%=item.getPrice()%>￥
							</td>
						</tr>
						<tr>
                 			<td>购买数量：<span id="sub" onclick="sub();">-</span><input type="text" id="number" name="number" value="1" size="2"/><span id="add" onclick="add();">+</span></td>
               			</tr> 
		             </table>
		             <div id="cart">
		               	<img src="images/buy_now.png"/>
		               	<a href="javascript:selflog_show(<%=item.getId()%>);"><img src="images/in_cart.png"/></a>
		               	<a href="CartServlet?action=show"><img src="images/view_cart.jpg"/></a>
		              
		             </div>
				</td>
				<%
				}
			%>
				<%
				String s = "";
				//从客户端获得Cookies集合 
				Cookie[] cookies = request.getCookies();
				//遍历这个集合，如果Cookies集合已经存在名字为“ListViewCookie”的Cookies，则把这个Cookies的赋给s
				if (cookies != null && cookies.length > 0) {
					for (Cookie c : cookies) {
						if (c.getName().equals("readList")) {
							s = c.getValue();
						}
					}
				}
				//将最近浏览过的商品ID存入字符串s中，取出的时候只需要把字符串用“，”进行分隔就可以进行提取 
				String id = request.getParameter("id");
				String[] array = s.split(",");
				//这里还应该判断商品ID是否已经存在于list当中，如果存在，则删除原来ID，在最后添加上商品ID
				s = DBItems.save(s, id);
				Cookie cookie = new Cookie("readList", s);
				response.addCookie(cookie);//一定要记得将cookie对象添加进行来
			%>
				<!-- 浏览过的商品 -->
				<td width="30%" bgcolor="#EEE" align="center"><br> <b>您浏览过的商品</b><br>
					<!-- 循环开始 --> <%
		 	//获取最近浏览的五个商品详细信息 
		 	ArrayList<Items> itemlist = DBItems.getLastFive(s);
		 	if (itemlist != null && itemlist.size() > 0) {
		 		for (Items i : itemlist) {
 			%>
					<div>
						<dl>
							<dt>
								<a href="details.jsp?id=<%=i.getId()%>"><img
									src="images/<%=i.getPicture()%>" width="120" height="90"
									border="1" /></a>
							</dt>
							<dd class="dd_name"><%=i.getName()%></dd>
							<dd class="dd_city">
								产地:<%=i.getCity()%>&nbsp;&nbsp;价格:<%=i.getPrice()%>
								￥
							</dd>
						</dl>
					</div> <%
 		}
 	}
 %> <!-- 循环结束 --></td>
			</tr>
		</table>
	</center>
</body>
</html>