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
    private static String actualYear;

    static{
        reloadDB();
    }

    private Model() {}

    public static XSSFRow getRow(int row) {
        XSSFSheet myExcelSheet = dbTable.getSheet("Публикации");
        XSSFRow targetRow = myExcelSheet.getRow(row);
        return targetRow;
    }

    public static String getCell(int row, int column) {
        XSSFCell cell = getRow(row).getCell(column);
        return cell == null ? "" : cell.getStringCellValue();
    }

    public static void reloadDB(){
        try(FileInputStream fis = new FileInputStream(pathToDBTable)){
            dbTable = new XSSFWorkbook(fis);
        }catch (IOException ex){
            throw new RuntimeException("Database table file not found");
        }
    }

    public static Book getBook(int bookNum) {
        XSSFRow row = getRow(bookNum);
        String yearCell = row.getCell(0).toString();
        if (!yearCell.isEmpty()){
            actualYear = yearCell;
        }
        return new Book(0,
                actualYear,
                row.getCell(1).getStringCellValue(),
                row.getCell(2).getStringCellValue(),
                row.getCell(3).getStringCellValue(),
                row.getCell(4).getStringCellValue());
    }

    public static int getRowCount(){
        return dbTable.getSheet("Публикации").getPhysicalNumberOfRows();
    }

}
