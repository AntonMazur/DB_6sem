import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import entities.Book;

public class Model {
    private static String pathToDBTable = "src/main/resources/dbtables/allPublications.xlsx";
    private static XSSFWorkbook dbTable;
    private static XSSFSheet myExcelSheet;
    private static String publicationsSheetName = "Публикации";
    private static String actualYear;

    public static void init(){
        reloadDB();
    }

    private Model() {}

    public static XSSFRow getRow(int row) {
        XSSFRow targetRow = myExcelSheet.getRow(row);
        return targetRow;
    }

    public static String getCell(int row, int column) {
        XSSFCell cell = getRow(row).getCell(column);
        return cell == null ? "" : cell.getStringCellValue();
    }

    public static String getCell(XSSFRow row, int column) {
        XSSFCell cell = row.getCell(column);
        return cell == null ? "" : cell.getStringCellValue();
    }

    public static void reloadDB(){
        MainWindow.executeParallel(() -> {
            try(FileInputStream fis = new FileInputStream(pathToDBTable)){
                dbTable = new XSSFWorkbook(fis);
            }catch (IOException ex){
                throw new RuntimeException("Database table file not found");
            }
            myExcelSheet = dbTable.getSheet(publicationsSheetName);
        });
    }

    public static Book getBook(int bookNum) {
        XSSFRow row = getRow(bookNum);
        String yearCell = row.getCell(0).toString();
        if (!yearCell.isEmpty()){
            actualYear = yearCell;
        }
        return new Book(0,
                actualYear,
                getCell(row, 1),
                getCell(row, 1),
                getCell(row, 1),
                getCell(row, 1));
    }

    public static Book[] getAllBooks(){
        Book[] books = new Book[getRowCount()];
        for (int i = 0; i < getRowCount(); i++) {
            books[i] = getBook(i);
        }
        return books;
    }

    public static int getRowCount(){
        return myExcelSheet.getPhysicalNumberOfRows();
    }

}
