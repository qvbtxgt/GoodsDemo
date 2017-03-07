package com.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import com.entity.Items;

public class DBItems {
	private static final String driver = "com.mysql.jdbc.Driver"; // 数据库驱动
	private static final String url = "jdbc:mysql://localhost:3306/my?useUnicode=true&characterEncoding=UTF-8";
	private static final String username = "root";// 数据库的用户名
	private static final String password = "";// 数据库的密码

	private static Connection conn = null;

	// 静态代码块负责加载驱动
	static {
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	// 单例模式返回数据库连接对象
	public static Connection getConnection() throws Exception {
		if (conn == null) {
			conn = DriverManager.getConnection(url, username, password);
			return conn;
		} else {
			return conn;
		}
	}

	public static void main(String[] args) {
		// 测试数据库是否连接成功
		// Connection conn;
		// try {
		// conn = DBItems.getConnection();
		// if(conn != null){
		// System.out.println("连接成功！");
		// }else{
		// System.out.println("连接失败！");
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		String s = "1,2,3,4,";
		System.out.println("原来的字符串：" + s);
		String id = "2";
		s = save(s, id);
		System.out.println("添加重复ID后的字符串：" + s);
		id = "6";
		s = save(s, id);
		System.out.println("添加没有重复ID后的字符串：" + s);
	}

	// 实现最近浏览商品ID存储（ID已经存在的，把原来位置的去掉，在最后重新加入；原来不存在的则直接在最后加入）
	public static String save(String s, String id) {
		// 这里还应该判断商品ID是否已经存在于list当中，如果存在，则删除原来ID，在最后添加上商品ID
		StringBuilder list = new StringBuilder();
		for (int i = 0; i < s.length(); i++) {
			list.append(s.charAt(i));
		}
		int index = list.indexOf(id);
		if (index != -1) {
			list.delete(index, index + 2);
		}
		list.append(id + ",");
		s = list.toString();
		return s;
	}

	// 查看所有Items信息
	public static ArrayList<Items> getAllItems() {
		ArrayList<Items> list = new ArrayList<Items>();
		Connection conn = null;
		PreparedStatement presta = null;
		ResultSet rset = null;
		try {
			conn = DBItems.getConnection();
			String sql = "select * from items";
			presta = conn.prepareStatement(sql);
			rset = presta.executeQuery();
			//将数据库中表的字段与创建的对象相对应
			while (rset.next()) {
				Items item = new Items();
				item.setId(rset.getInt("id"));
				item.setCity(rset.getString("city"));
				item.setName(rset.getString("name"));
				item.setNumber(rset.getInt("number"));
				item.setPrice(rset.getInt("price"));
				item.setPicture(rset.getString("picture"));
				list.add(item);
			}
			return list;
		} catch (Exception e) {
			return null;
		} finally {
			// 释放数据集对象，顺序为从小到大释放
			if (rset != null) {
				try {
					rset.close();
					rset = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// 释放语句对象
			if (presta != null) {
				try {
					presta.close();
					presta = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 根据商品编号获得商品信息
	public static Items getItemByID(int id) {
		Items item = new Items();
		Connection conn = null;
		PreparedStatement presta = null;
		ResultSet rset = null;
		try {
			conn = DBItems.getConnection();
			String sql = "select * from items where id = ?";
			presta = conn.prepareStatement(sql);
			presta.setInt(1, id);
			rset = presta.executeQuery();
			if (rset.next()) {
				item.setId(rset.getInt("id"));
				item.setCity(rset.getString("city"));
				item.setName(rset.getString("name"));
				item.setNumber(rset.getInt("number"));
				item.setPrice(rset.getInt("price"));
				item.setPicture(rset.getString("picture"));
			}
			return item;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			// 释放数据集对象，顺序为从小到大释放
			if (rset != null) {
				try {
					rset.close();
					rset = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			// 释放语句对象
			if (presta != null) {
				try {
					presta.close();
					presta = null;
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 获取最近前五条浏览记录
	public static ArrayList<Items> getLastFive(String s) {
		ArrayList<Items> list = new ArrayList<>();
		if (s != null && s.length() > 0) {
			String[] array = s.split(",");
			if (array.length <= 5) {
				for (int i = array.length - 1; i >= 0; i--) {
					list.add(DBItems.getItemByID(Integer.parseInt(array[i])));
				}
			} else {
				for (int i = array.length - 1; i >= array.length - 5; i--) {
					list.add(DBItems.getItemByID(Integer.parseInt(array[i])));
				}
			}

		}
		return list;
	}
}
