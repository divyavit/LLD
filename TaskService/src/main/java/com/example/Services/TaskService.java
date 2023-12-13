package com.example.Services;

public class TaskService {

    public Task createTask(String title, String creator, TaskType type, TaskStatus status, String dueDate, String assignee) {
        Task task = new Task(title, creator, type, status, dueDate, assignee);
        tasks.add(task);
        return task;
    }

    public SubTask createSubTask(String title, SubTaskStatus status, String linkageWithStory) {
        return new SubTask(title, status, linkageWithStory);
    }
    
}
