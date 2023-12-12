package Repository;

import Model.Issue;
import org.apache.commons.lang3.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

public class IssueRepository {
    public static Map<String, Issue> bookIssueList = new HashMap<>();

    public static void borrowBook(Issue issue){
        if(!ObjectUtils.isEmpty(issue))
            bookIssueList.put(issue.getBookId(), issue);
    }

    public static void updateIssueList(String bookCopyId){
        if(bookIssueList.containsKey(bookCopyId))
            bookIssueList.remove(bookCopyId);
    }
}
