package com.example.Services;

import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.models.Sprint;
import com.example.models.Task;
import com.example.models.TaskStatus;
import com.example.models.TaskType;

public class TaskPlannerService {
    
    public void displayUserTasks(String user,List<Task> tasks){
        System.out.println("Displaying User Tasks:");
        Map<TaskType, List<Task>> userTasks = new HashMap<>();
        for(Task task: tasks){
            if(user.equals(task.getAssignee())){
                userTasks.computeIfAbsent(task.getType(),k -> new ArrayList<>()).add(task);
            }
        }
        if(userTasks != null){
            for(Map.Entry<TaskType,List<Task>> entry: userTasks.entrySet()){
                System.out.println(entry.getKey() + " tasks: "+ entry.getValue());
            }
        }
        if(userTasks.size() == 0){
            System.out.println("Sorry User Tasks doest not exists");
        }
    }

    public void displaySprintSnapshot(Sprint sprint,List<Sprint> sprints){
        System.out.println("Displaying Sprint snapshot");
        if(sprints.isEmpty() || !sprints.contains(sprint)){
            System.out.println(" Sorry sprint doest not exists");
            return;
        }
         Map<String,List<Task>> snapshot = new HashMap<>();
         List<Task> delayed = new ArrayList<>();
         List<Task> allTasks = new ArrayList<>();
         List<Task> onTrack = new ArrayList<>();
         for(Task task: sprint.getStories()) {
            allTasks.add(task);
            if(task.getDueDate() != null && !TaskStatus.COMPLETED.equals(task.getStatus())){
                if(task.getDueDate().before(new Date())) {
                    delayed.add(task);
                }
                else{
                    onTrack.add(task);
                }
            }
        }
        snapshot.put("All Tasks",allTasks);
        snapshot.put("Delayed",delayed);
        snapshot.put("On Track",onTrack);
        if(snapshot != null){
            for(Map.Entry<String,List<Task>> entry: snapshot.entrySet()){
                System.out.println(entry.getKey() + " tasks: "+ entry.getValue());
            }
        }
    }
}
