package system.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import system.entity.Transaction;
import system.entity.User;

import java.util.List;

public interface TransactionRepository extends PagingAndSortingRepository<Transaction, Long> {

    @Query(" select t " +
            "from Transaction t " +
            "where t.sender.id = :acc_id " +
            "or t.reciever.id = :acc_id " +
            " order by t.date desc")
    List<Transaction> findFirst10Operation(@Param("acc_id")  long acc_id);

    @Query(value="select * from transaction t  where t.sender_account_id = ?1 or t.receiver_account_id = ?1 order by t.date desc limit ?3 offset ?2",
            nativeQuery = true)
    List<Transaction> findOperationsForPaddination(long acc_id, int offset, int limit);

    @Query(value="select count(*) from transaction t  where t.sender_account_id = ?1 or t.receiver_account_id = ?1",
            nativeQuery = true)
    long countOperations(long acc_id);


    @Query("select t" +
            " from Transaction t " +
            "where t.sender.id = :acc_id " +
            "or t.reciever.id = :acc_id  " +
            "order by t.date desc")
    Page<Transaction> findAllOperationForAccount(@Param("acc_id")  long acc_id, Pageable pageable);

}
