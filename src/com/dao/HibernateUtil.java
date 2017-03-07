package com.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

public class HibernateUtil {
	private static SessionFactory sFactory;
	private static Session session;
	
	static{
		//创建Configration对象，读取hibernate.cfg.xml文件，完成初始化
		Configuration cfg = new Configuration().configure();
		ServiceRegistry registry = new ServiceRegistryBuilder().applySettings(cfg.getProperties()).buildServiceRegistry();
		sFactory = cfg.buildSessionFactory(registry);
	}
	
	//获取SessionFactory
	public static SessionFactory getSessionFactory(){
		return sFactory;
	}
	
	//获取Session
	public static Session getSession(){
		session = sFactory.openSession();
		return session;
	}
	
	//关闭Session
	public static void closeSession(){
		if(session != null){
			session.close();
		}
	}
}
