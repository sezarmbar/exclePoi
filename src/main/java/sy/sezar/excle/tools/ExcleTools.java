package sy.sezar.excle.tools;

import java.io.File;
import java.io.IOException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import sy.sezar.excle.enity.Customers;
import sy.sezar.excle.repository.CustomersRepository;

@Component
public class ExcleTools {

  @Autowired
  CustomersRepository customersRepository;
  
  private final String SAMPLE_XLSX_FILE_PATH = "resources/anas.xlsx";
  private int nameIndex = 0;
  private int addressIndex = 2;
  private int postIndex = 1;
  private int klassIndex = 3;
  private static int startFrom = 1;

  private void writeInDatabase(Customers customer) {
    customersRepository.save(customer);
  }

  public void readFromExcle() throws IOException, InvalidFormatException {
    Workbook workbook = WorkbookFactory.create(new ClassPathResource("anas.xlsx").getFile());
    Sheet sheet = workbook.getSheetAt(0);
    int startRow = 0;
    for (Row row : sheet) {
      if (startRow > startFrom) {
        Customers customer = new Customers();
        customer.setName(row.getCell(nameIndex).toString());
        setAddress(row.getCell(addressIndex), customer);
        customer.setPost(setPost(row.getCell(postIndex)));
        customer.setOwerCustomer(row.getCell(klassIndex).getStringCellValue());
        writeInDatabase(customer);
      }
      startRow++;
    }
    workbook.close();
  }

  private void setAddress(Cell cell, Customers customer) {
    String address = cell.getStringCellValue();
    address = address.replace("straße", "").replaceAll("\\s+", "");
    address = address.replace("strasse", "").replaceAll("\\s+", "");
    address = address.replace("str", "").replaceAll("\\s+", "");
    address = address.replace("-", "").replaceAll("\\s+", "");
    address = address.replaceAll("[\\.$|,|;|']", "");
    customer.setAddressToCompare(address.toLowerCase());
    customer.setAddress(cell.getStringCellValue());
  }

  private int setPost(Cell cell) {
    return (int) cell.getNumericCellValue();
  }

  private boolean setOwerCustomer(Cell cell) {
    if (cell.getStringCellValue().equals("kunde") )
      return true;
    return false;
  }

}

