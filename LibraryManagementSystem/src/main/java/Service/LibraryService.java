package Service;

import CustomException.DuplicateBookIdException;
import CustomException.InvalidBookCopyId;
import CustomException.NotAvailableException;
import Model.Book;
import Model.Library;
import Repository.LibraryRepository;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.ObjectUtils;


import java.util.*;

@NoArgsConstructor
public class LibraryService {

    Library library;
    public static TreeSet<Integer> rackSet = null;
    public LibraryService(int rackSize) {
        this.library = new Library(rackSize);
        System.out.println("Created library with "+ rackSize +" racks");
        rackSet = new TreeSet<>();
        for(int i=1; i<=rackSize ; i++){
            rackSet.add(i);
        }
    }

    public void addBook(String bookId, String title, String[] authors, String[] publishers, String[] bookCopyIds)
            throws DuplicateBookIdException, NotAvailableException {
        List<String> authorList = Arrays.asList(authors);
        List<String> publisherList = Arrays.asList(publishers);
        List<String> bookCopyList = Arrays.asList(bookCopyIds);
        Book book = new Book(bookId, title, authorList, publisherList, bookCopyList);
        LibraryRepository.addBook(book);

        String racksAssigned = "";
        int rack = 0;

        for (String bookCopy : bookCopyIds) {
            rack = assignRack();
            racksAssigned += rack + " ";
            LibraryRepository.addBookItemToRack(bookCopy, rack);
        }
        System.out.println("Added Book to racks:" + racksAssigned);
    }

    private static int assignRack() throws NotAvailableException {
        if (rackSet.isEmpty()) {
            throw new NotAvailableException("Rack not available");
        }
        return rackSet.pollFirst();
    }

    public int addBookCopyToRack(String bookCopyId) throws NotAvailableException, InvalidBookCopyId {
        if(LibraryRepository.rackBookMap.containsKey(bookCopyId)) throw new InvalidBookCopyId("Invalid Book Copy Id");
        int rackNumber = assignRack();
        LibraryRepository.rackBookMap.put(bookCopyId, rackNumber);
        return rackNumber;
    }

    public void removeBook(String bookCopyId) throws InvalidBookCopyId {

        if(LibraryRepository.rackBookMap.containsKey(bookCopyId)) {
            int key = LibraryRepository.rackBookMap.remove(bookCopyId);
            System.out.println("Removed book copy: "+ bookCopyId+" from rack: "+key);
            rackSet.add(key);
        }
        else throw new InvalidBookCopyId("Invalid Book Copy ID");
    }

    public String getAvailableBookCopy(String bookId) throws InvalidBookCopyId {
        Book book = LibraryRepository.bookIdsMap.get(bookId);
        if (ObjectUtils.isEmpty(book))
            throw new InvalidBookCopyId("Invalid Book ID");
        else {
            List<String> bookCopy = book.getBookCopyIds();
            for (String bookC: bookCopy){
                if(isBookCopyAvailable(bookC)) return bookC;
            }
        }
        return null;
    }

    public boolean isBookCopyAvailable(String bookCopyId){
        if(LibraryRepository.rackBookMap.containsKey(bookCopyId)){
            int key = LibraryRepository.rackBookMap.remove(bookCopyId);
            rackSet.add(key);
            System.out.println("Borrowed Book from rack: "+ key);
            return true;
        }
        return false;
    }

    public boolean isValidBookCopy(String bookCopyId){
        for(Map.Entry<String, Book> map : LibraryRepository.bookIdsMap.entrySet()){
            List<String> bookIds = map.getValue().getBookCopyIds();
            boolean exists = bookIds.stream().filter(id -> id.equals(bookCopyId)).findFirst().isPresent();
            if(exists) return true;
        }

        return false;
    }
}
