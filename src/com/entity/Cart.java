package com.entity;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Cart {
	private HashMap<Items, Integer> goods;//购物车集合
	private Double totalPrice;//购物车总金额
	
	public Cart() {
		goods = new HashMap<Items,Integer>();
		totalPrice = 0.0;
	}
	
	public HashMap<Items, Integer> getGoods() {
		return goods;
	}

	public void setGoods(HashMap<Items, Integer> goods) {
		this.goods = goods;
	}

	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	//添加商品进购物车的方法，并且保证购物车中不出现重复商品
	public boolean addGoods(Items item,int number){
		if (goods.containsKey(item)) {
			number = goods.get(item)+number;
		}
		goods.put(item, number);
		calculatePrice();//重新计算购物车的总金额
		return true;
	}
	
	//删除商品的方法
	public boolean deleteGoods(Items item){
		goods.remove(item);
		calculatePrice();//重新计算购物车的总金额
		return true;
	}
	
	//统计购物车的总金额
	public double calculatePrice(){
		double sum = 0.0;
		Set<Items> set = goods.keySet();//获得键的集合
		Iterator<Items> it = set.iterator();//获得迭代器对象
		while(it.hasNext()){
			Items item = it.next();
			sum += item.getPrice()*goods.get(item);
		}
		this.setTotalPrice(sum);//设置购物车的总金额
		return getTotalPrice();
	}
	
	
	
	/*
	 * 获取Hashmap集合的两种方法，及比较
	 * Set<Map.Entry<K,V>> entrySet()  返回此映射中包含的映射关系的 set 视图。
	 * Set<K>              keySet()    返回此映射中包含的键的 set 视图。
	 * 对于keySet其实是遍历了2次，一次是转为iterator，一次就从hashmap中取出key所对于的value。
	 * 而entryset只是遍历了第一次，他把key和value都放到了entry中，所以就快了。
	 */
	public static void main(String[] args) {
		Items item1 = new Items(1,"沃特篮球鞋","温州",200,500,"001.jpg");
		Items item2 = new Items(2,"李宁运动鞋","广州",300,500,"002.jpg");
		Items item3 = new Items(1,"沃特篮球鞋","温州",200,500,"001.jpg");
		Cart cart = new Cart();
		cart.addGoods(item1, 2);
		cart.addGoods(item2, 2);
		//两次添加相同的商品时，保证购物车中不出现相同商品，应该只修改该商品的数量
		cart.addGoods(item3, 3);
		cart.addGoods(item2, 2);
		//遍历购物商品的集合
		Set<Map.Entry<Items, Integer>> set = cart.getGoods().entrySet();
		for (Entry<Items, Integer> entry : set) {
			System.out.println(entry);
		}
		System.out.println("购物车总金额："+cart.calculatePrice());
		
	}
}
