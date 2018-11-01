package derby;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

import org.hibernate.Session;

import lombok.extern.java.Log;

@Log
public class Client {

	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("test");  
		EntityManager em = emf.createEntityManager();                               
		EntityTransaction tx = em.getTransaction();                                 

    	
        Student student1 = new Student(1,"Sam");
        Student student2 = new Student(2,"Joshua");
        Student student3 = new Student(3,"Peter");
        
        List<Student> students = Arrays.asList(student1,student2,student3);
        
        College university = new College(
        		1,
        		"CAMBRIDGE", 
        		students
        		);
 
		  tx.begin();                                                               
		  em.persist(university);  
		  em.flush();
		  tx.commit();                                                              
		  
//		  Query query = em.createQuery("SELECT c FROM student c");
//		  List<Student> list = (List<Student>) query.getResultList();
//		  log.info(list.toString());
		  
		  em.close();
		  log.info("end.");      
	}

}
