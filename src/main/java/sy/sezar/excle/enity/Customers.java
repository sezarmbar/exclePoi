package sy.sezar.excle.enity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Customers {
  @Id
  @GeneratedValue
  private Long id;
  private String name;
  private String address;
  private String addressToCompare;  
  private int hausNumber;
  private int post;
  private String owerCustomer; 
}
