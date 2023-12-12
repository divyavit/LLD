package Model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Issue {

    private String id;
    private User user;
    private String bookId;
    private LocalDate issuedDate;
    private LocalDate dueDate;
//    private IssueStatus issueStatus;
}
