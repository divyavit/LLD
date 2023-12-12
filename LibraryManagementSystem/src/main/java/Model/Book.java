package Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Book {

    private String id;
    private String title;
    private List<String> authors;
    private List<String> publishers;
    private BookStatus bookStatus;
    private BookFormat bookFormat;
    private int publicationDate;
    private double price;
    List<String> bookCopyIds;

    public Book(String id, String title, List<String> authors, List<String> publishers, List<String> bookCopyIds) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.publishers = publishers;
        this.bookCopyIds = bookCopyIds;
    }
}
