import system.entity.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Test {


    public static void main(String[] args) {

        EntityManagerFactory emf = Persistence.createEntityManagerFactory("db");
        EntityManager em = emf.createEntityManager();

        User user = new User("Petya", "pass12bc23");

        EntityTransaction et = em.getTransaction();
        et.begin();
        em.persist(user);
        et.commit();

        em.close();
        emf.close();


    }
}
