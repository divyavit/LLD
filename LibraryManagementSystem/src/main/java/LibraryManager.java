import CustomException.*;
import Model.User;
import Service.IssueService;
import Service.LibraryService;
import Service.UserService;

import java.util.Scanner;

public class LibraryManager {

    public static void main(String[] args)
            throws DuplicateBookIdException, NotAvailableException, InvalidBookCopyId, OverlimitException, UserDoesNotExists {
        UserService userService = new UserService();
        IssueService issueService = new IssueService();
        LibraryService libraryService = null;

        //Creating some users
        try {
            userService.addUser("user1", "Uone", "uone@gmail.com");
            userService.addUser("user2", "Utwo", "utwo@gmail.com");
            userService.addUser("user3", "Uthree", "uthree@gmail.com");
            userService.addUser("user4", "Ufour", "ufour@gmail.com");
        } catch (Exception e){
            System.out.println(e);
        }

        Scanner scanner = new Scanner(System.in);

        while(true) {
            String command = scanner.nextLine();
            String[] commands = command.split(" ");
            String commandType = commands[0];

            if(commandType.equals("exit"))
                break;

            switch (commandType){
                case "create_library":
                    //create_library 10
                    libraryService = new LibraryService(Integer.parseInt(commands[1]));
                    break;
                case "add_book":
                    //add_book 1 book1 author1,author2 publisher1 book_copy1,book_copy2,book_copy3
                    //add_book 2 book2 author2,author3 publisher2,publisher3 book_copy4,book_copy5,book_copy6,book_copy7,book_copy8,book_copy9,book_copy10
                    //add_book 3 book3 author2 publisher2 book_copy11,book_copy12,book_copy13
                    String[] authors = commands[3].split(",");
                    String[] publishers = commands[4].split(",");
                    String[] bookCopyIds = commands[5].split(",");
                    libraryService.addBook(commands[1], commands[2], authors, publishers, bookCopyIds);
                    break;
                case "remove_book_copy":
                    //remove_book_copy book_copy1
                    libraryService.removeBook(commands[1]);
                    break;
                case "borrow_book":
                    //borrow_book 1 user1 2020-12-31
                    issueService.issueBook(commands[1], commands[2], commands[3]);
                    break;
                case "borrow_book_copy":
                    //borrow_book_copy book_copy1 user1 2020-12-31
                    issueService.issueBookCopy(commands[1], commands[2], commands[3]);
                    break;
                case "return_book_copy":
                    //return_book_copy book_copy1
                    issueService.returnBook(commands[1]);
                    break;
                case "print_borrowed":
                    //print_borrowed user1
                    issueService.printBorrowed(commands[1]);
                    break;
                default:
                    break;
            }
        }


    }
}
