package entities;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.experimental.Accessors;
import mainWin.MainWindow;
import org.hibernate.validator.constraints.Length;
import utilities.BookParam;
import utilities.Language;

import javax.persistence.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
@Table(name = "library")
@Data
@Accessors(chain = true)
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int dbId;
    private int id;
    @Column(name="year")
    private int year;
    @Column(name = "authors")
    private String authors;
    @Column(name = "name")
    private String name;
    @Column(name = "edition")
    private String edition;
    @Column(name="outputData")
    private String outputData;
    @Column(name="bookDesctiption", length = 500)
    private String bookDescription;
    @Column(name="WOS")
    private String WOS;
    @Column(name="SCOPULUS")
    private String SCOPULUS;
    @Column(name="DOI")
    private String DOI;
    @Column(name="fileLink")
    private String fileLink;

    @ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
            name = "library_authors",
            joinColumns = { @JoinColumn(name = "dbId") },
            inverseJoinColumns = { @JoinColumn(name = "id") }
    )
    private Collection<Author> authorSet = new ArrayList<Author>();

    private static int idGenerator = 0;
    private static String URL_FILE_PREFIX = "file://";

    {
        year = 0;
        authors = "";
        name = "";
        edition = "";
        outputData = "";
        bookDescription = "";
        WOS = "";
        SCOPULUS = "";
        DOI = "";
        fileLink = "";
    }

    @java.beans.ConstructorProperties({"id", "year", "name", "authors", "edition", "outputData"})
    public Book(int year, String authors, String name, String edition, String outputData, String DOI, String SCOPULUS, String WOS, String fileLink) {
        this.id = ++idGenerator;
        updateBook(year, authors, name, edition, outputData, DOI, SCOPULUS, WOS, fileLink);
    }

    public Book() {}

    public boolean isParamContainsKey(BookParam param, String key){
        switch(param) {
            case YEAR: return year == Integer.valueOf(key);
            case NAME: return name.contains(key);
            case AUTHORS: return authors.contains(key);
            case EDITION: return edition.contains(key);
            case OUTPUT_DATA: return outputData.contains(key);
            default: throw new IllegalArgumentException();
        }
    }

    public static void resetCounter(){
        idGenerator = 0;
    }

    public void setNextId(){
        id = ++idGenerator;
    }
    
    public void updateBook(int year, String authors, String name, String edition, String outputData, String DOI, String SCOPULUS, String WOS, String fileLink){
        this.year = year;
        this.authors = authors;
        this.name = name;
        this.edition = edition;
        this.outputData = outputData;
        this.DOI = DOI == null ? "" : DOI;
        this.SCOPULUS = SCOPULUS == null ? "" : SCOPULUS;
        this.WOS = WOS == null ? "" : WOS;
        this.fileLink = fileLink;
        updateBookDescription();
    }

    public void updateBookDescription(){
        bookDescription = authors + " " +
                          name + " " +
                          edition + " " +
                          year + " " +
                          outputData + " " +
                          getIdentifiersSummary();
    }

    /*public Book setFileLink(String path){
        fileLink = URL_FILE_PREFIX + path;
        return this;
    }*/

    @SneakyThrows
    public void openPublicationIfExist(){
        if (!fileLink.equals("")){
            URI fileURI = new File(fileLink).toURI();
            MainWindow.executeParallel(() -> {
                try{
                    Desktop.getDesktop().browse(fileURI);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            });
        }
    }

    public Language getLanguage(){
        return (int) name.charAt(0) > 1000 && (int) name.charAt(1) > 1000
                ? Language.RUSSIAN
                : Language.ENGLISH;
    }

    public Collection<Author> getAuthorsSet() {
        return authorSet;
    }

    public void addAuthor(Author author) {
        authorSet.add(author);
    }

    private String getIdentifiersSummary(){
        if (DOI.equals("") & SCOPULUS.equals("") & WOS.equals("")){
            return "";
        }
        return "(" + ((!DOI.equals("") ? "DOI: " + DOI + " ": "") +
               (!SCOPULUS.equals("") ? "SCOPUS: " + SCOPULUS + " ": "") +
               (!WOS.equals("") ? "WOS: " + WOS : "")) + ")";
    }
}
