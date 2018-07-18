package system.repository.implemention;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import system.entity.User;
import system.repository.UserRepository;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    @PersistenceContext(unitName = "db")
    private EntityManager em;


    @Override
    public <S extends User> S save(S s) {
        return null;
    }

    @Override
    public <S extends User> Iterable<S> save(Iterable<S> iterable) {
        return null;
    }

    @Override
    public User findOne(Long aLong) {
        return null;
    }

    @Override
    public boolean exists(Long aLong) {
        return false;
    }

    @Override
    public Iterable<User> findAll() {

       // EntityManager em = emFactory.createEntityManager();
        Query query = em.createQuery("SELECT t FROM User t");
        return (List<User>) query.getResultList();
    }

    @Override
    public Iterable<User> findAll(Iterable<Long> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void delete(Long aLong) {

    }

    @Override
    public void delete(User user) {

    }

    @Override
    public void delete(Iterable<? extends User> iterable) {

    }

    @Override
    public void deleteAll() {

    }

}
