package com.entity;

@Table("items")
public class Items {
	
	@Column("id")
	private int id;
	
	@Column("name")
	private String name;
	
	@Column("city")
	private String city;
	
	@Column("price")
	private int price;
	
	@Column("number")
	private int number;
	
	@Column("picture")
	private String picture;
	
	public Items() {
	}
	
	public Items(int id,String name,String city,int price,int number,String picture) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.price = price;
		this.number = number;
		this.picture = picture;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	//重写toString方法
	public String toString(){
		return "商品编号："+id+"，商品名称："+name;
	}

	//重写equals、hashCode方法来判断是否为同一商品，注意判断字符串相等用equals
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.getId()+this.getName().hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		if (this==obj) {
			return true;
		}else{
			if (obj instanceof Items) {
				Items it = (Items)obj;
				if (this.getId()==it.getId() && this.getName().equals(it.getName())) {
					return true;
				}else{
					return false;
				}
			}else{
				return false;
			}
		}
	}
}
