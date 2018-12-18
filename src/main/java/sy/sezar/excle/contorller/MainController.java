package sy.sezar.excle.contorller;

import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sy.sezar.excle.enity.Customers;
import sy.sezar.excle.service.CompareService;
import sy.sezar.excle.service.CustomersService;
import sy.sezar.excle.tools.ExcleTools;


@RestController
@RequestMapping("/api/")
public class MainController {
  
  @Autowired CustomersService customersService;
  @Autowired CompareService compareService;
  @Autowired ExcleTools excletools;
  @GetMapping("insertOne")
  public void insert() {
    Customers customer = new Customers();
    customer.setName("mahmoud");
    customer.setAddress("dsfasdf dsafa dsf");
    customer.setPost(3343);
    customersService.insertOne(customer);
  }
  
  @GetMapping("insert")
  public void insertFromExcle() throws InvalidFormatException, IOException {
    excletools.readFromExcle();
  }
  
  @GetMapping("getById")
  public String getFromData() {
    return customersService.getById(3l).get().toString();
  }
  
  @GetMapping("compare")
  public void compare() throws IOException {
    compareService.compare();
  }
  
  
}
