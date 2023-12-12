package Repository;

import CustomException.DuplicateBookIdException;
import CustomException.NotAvailableException;
import Model.Book;

import java.util.*;

public class LibraryRepository {

    public static Map<String , Integer> rackBookMap = new LinkedHashMap<>();
    public static Map<String, Book> bookIdsMap = new HashMap<>();

    public static void addBook(Book book)
            throws DuplicateBookIdException, NotAvailableException {

        if (bookIdsMap.containsKey(book.getId()))
            throw new DuplicateBookIdException("Book with the same id already exists");
        else bookIdsMap.put(book.getId(), book);
    }

    public static void addBookItemToRack(String bookCopyId, int rackNumber){
        rackBookMap.put(bookCopyId, rackNumber);
    }

    public static int removeBook(String bookCopyId) {
        int rackKey = rackBookMap.get(bookCopyId);
        rackBookMap.remove(bookCopyId);
        return rackKey;
    }

    public static int getAvailableBook(String bookId){
        Book book = bookIdsMap.get(bookId);

        List<String> bookCopyIds = book.getBookCopyIds();
        for (String bookCopyId: bookCopyIds)
            return rackBookMap.getOrDefault(bookCopyId, -1);
        return -1;
    }
}
