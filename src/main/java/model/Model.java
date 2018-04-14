package model;

import entities.Author;
import mainWin.MainWindow;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import entities.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Model {
    private static String pathToDBTable = "src/main/resources/businesslogicdata/allPublications_edited.xlsx";
    private static XSSFWorkbook dbTable;
    private static XSSFSheet myExcelSheet;
    private static String publicationsSheetName = "Публикации";
    private static int actualYear;
    private static SessionFactory sessionFactory;
    private static Session currentSession;
    private static Transaction currentTransation;
    private static Set<Author> allAuthors = new HashSet<>();
    private static Set<Book> allBooks = new HashSet<>();

    public static void setSessionFactory(SessionFactory sessionFactory){
        Model.sessionFactory = sessionFactory;
    }

    public static void openSessionStartTransation() {
        currentSession = sessionFactory.openSession();
        currentTransation = currentSession.beginTransaction();
    }

    public static void commitTransationCloseSession() {
        currentTransation.commit();
        currentSession.close();
    }

    public static void updateBook(Book book){
        openSessionStartTransation();
        currentSession.update(book);
        commitTransationCloseSession();
    }

    public static void insertBook(Book book){
        boolean isAuthorExist;
        String[] authors = book.getAuthors().split(", ");
        for (String strAuthor : authors) {
            isAuthorExist = false;
            for (Author author : allAuthors) {
                if (author.getName().equals(strAuthor)) {
                    book.addAuthor(author);
                    author.addBook(book);
                    isAuthorExist = true;
                    break;
                }
            }
            if (!isAuthorExist) {
                Author author = new Author(strAuthor);
                allAuthors.add(author);
                book.addAuthor(author);
                author.addBook(book);
            }
        }
        openSessionStartTransation();
        currentSession.persist(book);
        commitTransationCloseSession();
    }

    private static void insertAuthor(Author author) {
        openSessionStartTransation();
        currentSession.persist(author);
        commitTransationCloseSession();
    }

    public static void insertBatchOfBooks(Book... books){
        openSessionStartTransation();
        for (Book book: books) {
            currentSession.save(book);
        }
        commitTransationCloseSession();
    }

    public static void insertBatchOfAuthors(Collection<Author> authors){
        openSessionStartTransation();
        for (Author author: authors) {
            currentSession.save(author);
        }
        commitTransationCloseSession();
    }

    public static void deleteBook(Book book) {
        openSessionStartTransation();
        currentSession.delete(book);
        commitTransationCloseSession();
    }
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
        int yearCell = ((Double) row.getCell(0).getNumericCellValue()).intValue();
        if (yearCell != 0){
            actualYear = yearCell;
        }
        Book book = new Book(
                actualYear,
                getCell(row, 1),
                getCell(row, 2),
                getCell(row, 3),
                getCell(row, 4),
                getCell(row, 9),
                getCell(row, 8),
                getCell(row, 7),
                getCell(row, 10));
        boolean isAuthorExist;
        String[] authors = book.getAuthors().split(", ");
        for (String strAuthor : authors) {
            isAuthorExist = false;
            for (Author author : allAuthors) {
                if (author.getName().equals(strAuthor)) {
                    book.addAuthor(author);
                    author.addBook(book);
                    isAuthorExist = true;
                    break;
                }
            }
            if (!isAuthorExist) {
                Author author = new Author(strAuthor);
                allAuthors.add(author);
                book.addAuthor(author);
                author.addBook(book);
            }
        }
        return book;

    }

    public static Book[] getAllBooks(){
        Book[] books = new Book[getRowCount() - 1];
        for (int i = 1; i < getRowCount(); i++) {
            books[i - 1] = getBook(i);
        }
        insertBatchOfBooks(books);
        return books;
    }

    public static int getRowCount(){
        return myExcelSheet.getPhysicalNumberOfRows();
    }

}
