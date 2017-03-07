import java.util.List;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Before;
import org.junit.Test;

import com.dao.HibernateUtil;
import com.entity.Grade;	
import com.entity.Students;

public class GradeTest {
		
	private Session session = null;
	@Before
	public void SetUp(){
		session = HibernateUtil.getSession();
	}
	
	@Test
	public void testGrade(){//一对多
		Transaction trans = session.beginTransaction();
		Grade grade = new Grade();
		grade.setGname("C++二班");
		grade.setGdesc("C++软件开发二班");
		Students s1= new Students();
		Students s2= new Students();
		s1.setSname("小二");
		s1.setGender("男");
		s2.setSname("凯凯");
		s2.setGender("男");
		//设置关联关系，在班级中添加学生
		grade.getStudents().add(s1);
		grade.getStudents().add(s2);
		session.save(grade);
		session.save(s1);
		session.save(s2);
		trans.commit();
		HibernateUtil.closeSession();
	}
	
	@Test
	public void testHQL(){
		String hql = " from Students ";
		Query query = session.createQuery(hql);
		List<Students> students = query.list();
		for (Students students2 : students) {
			System.out.println(students2);
		}
	}
	
	@Test
	public void testGradeHQL(){//HQL
		String hql = " select distinct gname from Grade g where g.gid>2";
		Query query = session.createQuery(hql);
		List<Object> grades = query.list();
		for (Object object : grades) {
			System.out.println(object);
		}	
	}
	
	@Test
	public void findStudents(){//通过Gid获取班级对应的学生信息
		Grade grade = (Grade) session.get(Grade.class, 2);
		System.out.println("Gid为2的班级名称为："+grade.getGname());
		Set<Students> students = grade.getStudents();//单向一对多，即一个班级对应多个学生
		for (Students stu : students) {
			System.out.println(stu.getSname()+":"+stu.getGender());
		}
	}
}
