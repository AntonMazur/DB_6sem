package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utilities.BookParam;

import javax.persistence.*;


@Entity(name = "library")
@Data
@AllArgsConstructor()
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="year")
    private String year;
    @Column(name = "name")
    private String name;
    @Column(name = "authors")
    private String authors;
    @Column(name = "edition")
    private String edition;
    @Column(name="otherData")
    private String otherData;

    public boolean isParamContainsKey(BookParam param, String key){
        switch(param) {
            case YEAR: return year.contains(key);
            case NAME: return name.contains(key);
            case AUTHORS: return authors.contains(key);
            case EDITION: return edition.contains(key);
            case OTHER_DATA: return otherData.contains(key);
            default: throw new IllegalArgumentException();
        }
    }
}
