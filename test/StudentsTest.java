import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.entity.Students;

public class StudentsTest {
	private SessionFactory sFactory;
	private Session session;
	private Transaction transaction;
	
	@Before
	public void init(){
		System.out.println("Test before");
		//创建配置对象 
		Configuration config = new Configuration().configure();
		//创建服务注册对象 
		ServiceRegistry registry = new ServiceRegistryBuilder().applySettings(config.getProperties()).buildServiceRegistry();
		//创建会话工厂对象
		sFactory = config.buildSessionFactory(registry);
		//会话对象 
		session = sFactory.openSession();
		session.doWork(new Work() {
			
			@Override
			public void execute(Connection arg0) throws SQLException {
				//Session对象就相当于JDBC里面的Connection，打印Connection的哈希码
				System.out.println("Connection HashCode："+arg0.hashCode());
			}
		});
		//开启事务
		transaction = session.beginTransaction();
	}
	
	@Test
	public void testStudents(){
		//生成学生对象
		Students s = new Students(1, "张三丰", "男", new Date(), "武当山");
		session.save(s);//保存对象进入数据库
		transaction.commit();//手动提交事务
		session.close();//关闭会话
		
		Session session2 = sFactory.openSession();
		transaction = session2.beginTransaction();
		s = new Students(2, "灭绝师太", "女", new Date(), "峨嵋山");
		session2.doWork(new Work() {
			
			@Override
			public void execute(Connection arg0) throws SQLException {
				// TODO Auto-generated method stub
				System.out.println("Connection HashCode："+arg0.hashCode());
			}
		});
		session2.save(s);
		transaction.commit();
	}
	
	@After
	public void destory(){
		System.out.println("Test destory");
		
		sFactory.close();//关闭会话工厂 
	}
}
