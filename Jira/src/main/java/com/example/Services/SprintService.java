package com.example.Services;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.example.models.Sprint;
import com.example.models.Story;
import com.example.models.Task;

public class SprintService {
    
    public Sprint createSprint(String name,List<Sprint> sprints){
        if(sprints.stream().anyMatch(s -> s.getName().equalsIgnoreCase(name))){
            System.out.println("Sprint already exist");
            return sprints.stream().filter(sprint -> sprint.getName().equalsIgnoreCase(name)).collect(Collectors.toList()).get(0);
        }
        System.out.println("Created sprint "+name);
        Sprint sprint = Sprint.builder().name(name).build();
        sprints.add(sprint);
        return sprint;
    }

    public void deleteSprint(Sprint sprint,List<Sprint> sprints){
        sprints.remove(sprint);
        System.out.println("Sprint sucessfuly removed");
    }

    public void addTaskToSprint(Sprint sprint,Task task,List<Sprint> sprints){
        if(sprints.stream().anyMatch(s -> s.getName().equalsIgnoreCase(sprint.getName()))){
            if(sprint.getStories().stream().noneMatch(s -> s.getTitle().equalsIgnoreCase(task.getTitle()))){
                System.out.println("Added " + task.getTitle() + " to "+sprint.getName());
                sprint.getStories().add(task);
            }
            else{
                System.out.println(task.getTitle() + " is already added to "+sprint.getName());
            }
        }
        else{
            System.out.println(sprint.getName()+" does not exists");
            return;
        }
    }
}
