package sy.sezar.excle.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import sy.sezar.excle.enity.Customers;
import sy.sezar.excle.repository.CustomersRepository;

@Service
public class CompareService {
  @Autowired
  CustomersRepository customersRepository;


  private final String SAMPLE_XLSX_FILE_PATH = "resources/anas.xlsx";
  private int nameIndex = 8;
  private int postIndex = 9;
  private int addressIndex = 10;
  private int klassIndex = 3;
  private static int startFrom = 1;

  public void compare() throws IOException {
    List<Customers> listCustomers = new ArrayList<>();
    List<Customers> listExistedCustomers = new ArrayList<>();


    Workbook workbook = WorkbookFactory.create(new ClassPathResource("anas.xlsx").getFile());
    Sheet sheet = workbook.getSheetAt(0);
    int startRow = 0;
    for (Row row : sheet) {
      Customers cusTmp = new Customers();
      if (startRow > startFrom) {
        Customers customer = new Customers();
        customer.setName(row.getCell(nameIndex).toString());
        setAddress(row.getCell(addressIndex), customer);
        customer.setPost(setPost(row.getCell(postIndex)));
        Optional<List<Customers>> custList =
            customersRepository.find(customer.getAddressToCompare(), customer.getPost());
        if (!custList.isPresent()) {
          listCustomers.add(customer);
        } else {
          listExistedCustomers.addAll(custList.get());
        }
      }
      startRow++;
    }
    workbook.close();
    wreiteToExcle(listCustomers, listExistedCustomers);
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

  private void wreiteToExcle(List<Customers> listCustomers, List<Customers> listExistedCustomers)
      throws IOException {
    String FILE_NAME = "MyFirstExcel.xlsx";
    XSSFWorkbook workbook = new XSSFWorkbook();
    XSSFSheet sheet = workbook.createSheet("Datatypes in Java");

    int rowNum = 0;
    System.out.println("Creating excel");

    Cell cell;
    Row row = sheet.createRow(rowNum++);
    cell = row.createCell(1);
    setCell(cell, "    New Customers     ");
    for (Customers customer : listCustomers) {
      row = sheet.createRow(rowNum++);
      int colNum = 0;
      cell = row.createCell(colNum++);
      setCell(cell, customer.getName());
      cell = row.createCell(colNum++);
      setCell(cell, customer.getPost());
      cell = row.createCell(colNum++);
      setCell(cell, customer.getAddress());
    }

    row = sheet.createRow(rowNum++);
    cell = row.createCell(1);
    setCell(cell, "    Existed interessant Customers     ");

    for (Customers customer : listExistedCustomers) {
      if (customer.getOwerCustomer().equals("interessant")) {
        row = sheet.createRow(rowNum++);
        int colNum = 0;

        cell = row.createCell(colNum++);
        setCell(cell, customer.getName());

        cell = row.createCell(colNum++);
        setCell(cell, customer.getPost());

        cell = row.createCell(colNum++);
        setCell(cell, customer.getAddress());

        cell = row.createCell(colNum++);
        setCell(cell, customer.getOwerCustomer());
      }
    }

    row = sheet.createRow(rowNum++);
    cell = row.createCell(1);
    setCell(cell, "    Existed kunde Customers     ");

    for (Customers customer : listExistedCustomers) {
      if (!customer.getOwerCustomer().equals("interessant")) {
        row = sheet.createRow(rowNum++);
        int colNum = 0;

        cell = row.createCell(colNum++);
        setCell(cell, customer.getName());

        cell = row.createCell(colNum++);
        setCell(cell, customer.getPost());

        cell = row.createCell(colNum++);
        setCell(cell, customer.getAddress());

        cell = row.createCell(colNum++);
        setCell(cell, customer.getOwerCustomer());
      }
    }
    try {
      FileOutputStream outputStream = new FileOutputStream(FILE_NAME);
      workbook.write(outputStream);
      workbook.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    System.out.println("Done");
  }

  private Cell setCell(Cell cell, Object obj) {
    if (obj instanceof String) {
      cell.setCellValue((String) obj);
    } else if (obj instanceof Boolean) {
      cell.setCellValue((Boolean) obj);
    } else if (obj instanceof Double) {
      cell.setCellValue((Double) obj);
    } else if (obj instanceof Integer) {
      cell.setCellValue((Integer) obj);
    }
    return cell;

  }

}
