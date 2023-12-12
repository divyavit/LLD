package Service;

import CustomException.InvalidBookCopyId;
import CustomException.NotAvailableException;
import CustomException.OverlimitException;
import CustomException.UserDoesNotExists;
import Model.Issue;
import Model.IssueStatus;
import Model.User;
import Repository.IssueRepository;
import Repository.LibraryRepository;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.UUID;

public class IssueService {

    private LibraryService libraryService;
    private UserService userService;

    public IssueService() {
        this.libraryService = new LibraryService();
        this.userService = new UserService();
    }

    public void issueBook(String bookId, String userId, String dueDate)
            throws InvalidBookCopyId, OverlimitException, UserDoesNotExists, NotAvailableException {

        if (!LibraryRepository.bookIdsMap.containsKey(bookId)) throw new InvalidBookCopyId("Invalid Book ID");
        else {
            boolean canUserBorrowBook = userService.canUserBorrowBook(userId);
            if (!canUserBorrowBook) throw new OverlimitException("Overlimit");

            String bookCopyId = libraryService.getAvailableBookCopy(bookId);
            if(StringUtils.isEmpty(bookCopyId)) throw new NotAvailableException("Book Copy Not Available");

            createNewIssue(userId, bookCopyId, LocalDate.parse(dueDate), IssueStatus.ISSUED);
        }
    }

    public void issueBookCopy(String bookCopyId, String userId, String dueDate)
            throws OverlimitException, UserDoesNotExists, NotAvailableException {

            boolean canUserBorrowBook = userService.canUserBorrowBook(userId);
            if (!canUserBorrowBook) throw new OverlimitException("Overlimit");

            if(!libraryService.isBookCopyAvailable(bookCopyId)) throw new NotAvailableException("Book Copy Not Available");

           createNewIssue(userId, bookCopyId, LocalDate.parse(dueDate), IssueStatus.ISSUED);
    }

    public void returnBook(String bookCopyId) throws InvalidBookCopyId, NotAvailableException {
        if(!libraryService.isValidBookCopy(bookCopyId)) throw new InvalidBookCopyId("Invalid Book Copy Id");

        int rackNumber = libraryService.addBookCopyToRack(bookCopyId);
        System.out.println("Returned book copy "+bookCopyId+" and added to rack: :" + rackNumber);
        IssueRepository.updateIssueList(bookCopyId);
    }

    public void printBorrowed(String userId) throws UserDoesNotExists {
            User user = userService.getUser(userId);
            if(user.getBooksBorrowed() != 0) {
                IssueRepository.bookIssueList.forEach((k,v) -> {
                    if(userId.equals(v.getUser().getId())){
                        System.out.println("Book Copy: " + v.getBookId() +" " + v.getDueDate());
                    }
                });
            }
    }

    private void createNewIssue(String userId, String bookCopyId, LocalDate dueDate, IssueStatus issueStatus) throws UserDoesNotExists {
        LocalDate currDate = LocalDate.now();
        Issue issue = new Issue(UUID.randomUUID().toString(), userService.getUser(userId), bookCopyId, currDate, dueDate);
        IssueRepository.borrowBook(issue);

        userService.updateBorrowCount(userId);
    }




}
