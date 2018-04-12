package entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authors")
@Data
@NoArgsConstructor
public class Author{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String name;
    @ManyToMany(mappedBy = "authorSet")
    private Collection<Book> books = new ArrayList<Book>();

    public Author(String name) {
        this.name = name;
    }

    public Collection<Book> getBooks(){
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    }
}
