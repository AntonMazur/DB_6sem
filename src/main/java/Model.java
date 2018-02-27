import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import entities.Book;

public class Model {
    private static String pathToDBTable = "src/main/resources/dbtables/allPublications.xlsx";
    private static XSSFWorkbook dbTable;

    static{
        reloadDB();
    }

    private Model() {}

    public static XSSFRow getRow() throws IOException{
        XSSFSheet myExcelSheet = dbTable.getSheet("Публикации");
        XSSFRow row = myExcelSheet.getRow(16);
        return row;
    }

    public static void reloadDB(){
        try(FileInputStream fis = new FileInputStream(pathToDBTable)){
            dbTable = new XSSFWorkbook(fis);
        }catch (IOException ex){
            throw new RuntimeException("Database table file not found");
        }
    }

}
