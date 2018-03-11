package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity(name = "library")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(name="year")
    private int editionYear;
    @Column(name = "edition")
    private String edition;
    @Column(name = "authors")
    private String authors;
    @Column(name = "name")
    private String name;
}
