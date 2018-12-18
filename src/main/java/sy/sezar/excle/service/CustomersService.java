package sy.sezar.excle.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sy.sezar.excle.enity.Customers;
import sy.sezar.excle.repository.CustomersRepository;
@Service
public class CustomersService {
  @Autowired
  CustomersRepository customersRepository;

  public void insertOne(Customers customer) {
    customersRepository.save(customer);
    
  }
  
  public Optional<Customers> getById(Long id) {
    return customersRepository.findById(id);
  }
}
