package sy.sezar.excle.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sy.sezar.excle.enity.Customers;
@Repository
public interface CustomersRepository extends JpaRepository<Customers, Long> {

  @Query("SELECT c FROM Customers c WHERE c.addressToCompare =:address and c.post = :post ")
  public Optional<List<Customers>> find(@Param("address")String address,@Param("post")int post);
}
