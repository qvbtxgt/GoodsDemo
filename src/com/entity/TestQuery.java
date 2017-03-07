package com.entity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.dao.DBItems;

public class TestQuery {
	public static void main(String[] args) {
		Items it1 = new Items();
		it1.setId(3);// 查询id为3的商品
		it1.setNumber(1000);

		Items it2 = new Items();
		it2.setId(7);
		it2.setCity("北京");// 查询地点为北京的商品

		Items it3 = new Items();
		it3.setPrice(120);// 查询价格为300的商品

		String sql1 = query(it1);
		String sql2 = query(it2);
		String sql3 = query(it3);
		
		System.out.println(sql1);
		System.out.println(sql2);
		System.out.println(sql3);
		
		//通过反向生成的SQL语句
		ArrayList<Items> list = getAllItems(sql2);
		if(list!=null && list.size()>0)
        {
            for(int i=0;i<list.size();i++){
               Items item = list.get(i);
               System.out.println("ID:"+item.getId()+"，名称:"+item.getName()+"，地点:"+item.getCity()+"，价格:"+item.getPrice());
            }
            
        }
	}

	/*
	 * 1、获取类类型 2、获取到table的名字 3、获取并遍历表中的所有字段 4、处理每个字段对应的SQL，获取字段的名字，字段的值，拼装SQL
	 */
	public static String query(Object obj) {
		StringBuilder str = new StringBuilder();
		// 1、获取类类型
		Class c = obj.getClass();

		// 2、获取到table的名字
		boolean exist = c.isAnnotationPresent(Table.class);// 判断指定类型的注释不否存在于此元素上
		if (!exist) {
			return null;
		}
		Table table = (Table) c.getAnnotation(Table.class);// 获取该元素的指定类型的注释，并强制类型转化
		String table_name = table.value();
		str.append("select * from ").append(table_name).append(" where 1=1");

		// 3、获取并遍历表中的所有字段
		Field[] fArray = c.getDeclaredFields();
		for (Field f : fArray) {
			// 4、处理每个字段对应的SQL，获取字段的名字，字段的值，拼装SQL
			boolean fExist = f.isAnnotationPresent(Column.class);
			if (!fExist) {
				continue;
			}
			Column column = f.getAnnotation(Column.class);
			String field_name = column.value();// 获取字段名，通过字段名合成获取字段的方法
			String method = "get" + field_name.substring(0, 1).toUpperCase() + field_name.substring(1);
			// 通过反射调用getId()、getName()等方法来获取类中的成员变量值
			Object field_value = null;
			try {

				Method getMethod = c.getMethod(method);// 通过类的方法名称获取对应的方法
				field_value = getMethod.invoke(obj);// 通过反射调用类的方法，不带参数

			} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				e.printStackTrace();
			}
			// 如果方法的返回值为空，则不添加到SQL中
			if (field_value == null || field_value instanceof Integer && (Integer) field_value == 0) {
				continue;
			}
			str.append(" and ").append(field_name);
			// 如果一个字符串的查询条件包含多个值，则把这些值用逗号隔开
			if (field_value instanceof String) {
				if (((String) field_value).contains(",")) {
					String[] sArray = ((String) field_value).split(",");
					str.append(" in (");
					for (String s : sArray) {
						// 需要判断类的方法返回值类型
						if (s instanceof String) {
							s = "'" + s + "'";
						}
						str.append(s).append(",");
					}
					str.deleteCharAt(str.length()-1);// 把最后一个逗号删除
					str.append(")");
				}else{
					field_value = "'" + field_value + "'";
					str.append("=").append(field_value);
				}
			}else{
				str.append("=").append(field_value);
			}
		}
		str.append(";");
		return str.toString();
	}
	
	// 查看所有Items信息
	public static ArrayList<Items> getAllItems(String sql) {
		ArrayList<Items> list = new ArrayList<Items>();
		Connection conn = null;
		PreparedStatement presta = null;
		ResultSet rset = null;
		try {
			conn = DBItems.getConnection();
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

}
