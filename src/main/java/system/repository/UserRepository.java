package system.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import system.entity.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User, Long>
{

    @Override
    User findOne(Long aLong);

    @Override
    boolean exists(Long aLong);

    @Override
    Iterable<User> findAll();

    @Override
    Iterable<User> findAll(Iterable<Long> iterable);

    @Override
    long count();

    @Override
    void delete(Long aLong);

    @Override
    void delete(User user);

    @Override
    void delete(Iterable<? extends User> iterable);

    @Override
    void deleteAll();
}
