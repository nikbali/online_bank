package system.repository;

import org.springframework.data.repository.CrudRepository;
import system.entity.User;



public interface UserRepository extends CrudRepository<User, Integer>
{

}
