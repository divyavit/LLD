package Service;

import CustomException.InvalidBookCopyId;
import Model.Book;
import Repository.LibraryRepository;

public class SearchService {

        public void geBookById(String bookId) throws InvalidBookCopyId {
                if(!LibraryRepository.bookIdsMap.containsKey(bookId)) throw new InvalidBookCopyId("Invalid Book Id");
                Book book = LibraryRepository.bookIdsMap.get(bookId);
                //FETCH ALL THE BOOK COPIES
                //FOR THE COPIES AVAILABLE ON RACK
                //PRINT THE BOOK COPY ID WITH THE RACK NUMBER
        }

        public void geBookByAuthor(String authorId){}

        public void geBookByPublisher(String publisherId){

        }
}
