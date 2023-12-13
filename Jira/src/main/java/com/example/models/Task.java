package com.example.models;

import java.util.Date;
import java.util.List;

import lombok.*;

@Data 
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Task {
    public String title;
    public String creator;
    public String assignee;
    public TaskStatus status;
    public TaskType type;
    public Date dueDate;
    private List<SubTask> subtasks;
}
