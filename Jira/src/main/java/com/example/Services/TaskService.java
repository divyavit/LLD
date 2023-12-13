package com.example.Services;
import com.example.models.*;
import java.util.*;
public class TaskService {

    public boolean isTransitionAllowed(Task task,TaskStatus newStatus){
        Map<TaskStatus,Set<TaskStatus>> allowedTransitions = getAllowedTransitions(task);
        Set<TaskStatus> allowedNextStatus = allowedTransitions.getOrDefault(task.getStatus(), Collections.emptySet());
        return allowedNextStatus.contains(newStatus);
    }

    Map<TaskStatus,Set<TaskStatus>> getAllowedTransitions(Task task){
        Map<TaskStatus,Set<TaskStatus>> allowedTransitions = new HashMap<>();
        if(TaskType.FEATURE.equals(task.getType())){
            allowedTransitions.put(TaskStatus.OPEN,new HashSet<>(Arrays.asList(TaskStatus.IN_PROGRESS)));
            allowedTransitions.put(TaskStatus.IN_PROGRESS,new HashSet<>(Arrays.asList(TaskStatus.TESTING,TaskStatus.DEPLOYED)));
            allowedTransitions.put(TaskStatus.TESTING,new HashSet<>(Arrays.asList(TaskStatus.DEPLOYED)));
            
        }

        return allowedTransitions;
    }

    public void markTaskAsCompleted(Task story){
        if(story.getType().equals(TaskType.STORY)){
            if(story.getSubtasks().stream().allMatch(subtask -> subtask.getStatus().equals(SubTaskStatus.COMPLETED))){
                System.out.println("Marked "+story.getTitle()+" as completed");
                story.setStatus(TaskStatus.COMPLETED);
            }
            else{
                System.out.println("All subtasks should be completed");
            }
        }
        else{
            System.out.println("Marked "+story.getTitle()+" as completed");
            story.setStatus(TaskStatus.COMPLETED);
        }
    }
    
    public Task createTask(String title,String creator,TaskType type,TaskStatus status,Date dueDate,String assignee){
        Task task = Task.builder().title(title).creator(creator).type(type).status(status).dueDate(dueDate).assignee(assignee).build();
        return task;
    }

    public SubTask createSubTask(String title,SubTaskStatus status,String linkageWithStory){
        return SubTask.builder().title(title).status(status).linkageWithStory(linkageWithStory).build();
    }

    public void changeTaskStatus(TaskStatus taskStatus,Task task){
        task.setStatus(taskStatus);
    }

    public void changeSubTaskStatus(SubTaskStatus taskStatus,SubTask task){
        task.setStatus(taskStatus);
    }

    public void changeAssignee(String newAssignee,Task task){
        task.setAssignee(newAssignee);
    }

    public void addSubTask(SubTask subTask,Task task){
        if(task.getType().equals(TaskType.STORY) && !subTask.getStatus().equals(SubTaskStatus.COMPLETED)){
            if(task.getSubtasks() == null){
                task.setSubtasks(new ArrayList<>());
            }
            task.getSubtasks().add(subTask);
            System.out.println(subTask.getTitle()+" added to task "+task.getTitle());
        }
        else{
            if(subTask.getStatus().equals(SubTaskStatus.COMPLETED)){
                System.out.println("Subtask cannot be in a completed state");
            }
            else{
                System.out.println("Sorry, cannot add subtasks as task is not a Story");
            }
            return;
        }
    }
    
}
