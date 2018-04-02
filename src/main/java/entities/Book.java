package entities;

import lombok.Data;
import lombok.experimental.Accessors;
import utilities.BookParam;

import javax.persistence.*;


@Entity(name = "library")
@Data
@Accessors(chain = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="year")
    private int year;
    @Column(name = "authors")
    private String authors;
    @Column(name = "name")
    private String name;
    @Column(name = "edition")
    private String edition;
    @Column(name="otherData")
    private String otherData;
    @Column(name="bookDesctiption")
    private String bookDescription;
    @Column(name="WOS")
    private String WOS;
    @Column(name="SCOPULUS")
    private String SCOPULUS;
    @Column(name="DOI")
    private String DOI;
    @Column(name="fileLink")
    private String fileLink;

    private static int idGenerator = 0;

    {
        year = 0;
        authors = "";
        name = "";
        edition = "";
        otherData = "";
        bookDescription = "";
        WOS = "";
        SCOPULUS = " ";
        DOI = "";
        fileLink = null;
    }

    @java.beans.ConstructorProperties({"id", "year", "name", "authors", "edition", "otherData"})
    public Book(int year, String authors, String name, String edition, String otherData) {
        this.id = ++idGenerator;
        this.year = year;
        this.authors = authors;
        this.name = name;
        this.edition = edition;
        this.otherData = otherData;
        updateBookDescription();
    }

    public Book() {}

    public boolean isParamContainsKey(BookParam param, String key){
        switch(param) {
            case YEAR: return year == Integer.valueOf(key);
            case NAME: return name.contains(key);
            case AUTHORS: return authors.contains(key);
            case EDITION: return edition.contains(key);
            case OTHER_DATA: return otherData.contains(key);
            default: throw new IllegalArgumentException();
        }
    }

    public static void resetCounter(){
        idGenerator = 0;
    }

    public void setNextId(){
        id = ++idGenerator;
    }

    public void updateBookDescription(){
        bookDescription = authors + " " +
                          name + " " +
                          edition + " " +
                          year + " " +
                          otherData + " " +
                          getIdentifiersSummary();
    }


    private String getIdentifiersSummary(){
        return (!DOI.equals("") ? "DOI " + DOI : "") +
               (!SCOPULUS.equals("") ? " SCOPULUS" + SCOPULUS: "") +
               (!WOS.equals("") ? " WOS " + WOS : "");
    }
}
