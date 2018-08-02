package system.repository;

import org.springframework.data.repository.CrudRepository;
import system.entity.User;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Long>
{
    User findByLogin(String login);

    User findByEmail(String email);

    User findByDocumentNumber(int document_number);

    List<User> findAll();
}
