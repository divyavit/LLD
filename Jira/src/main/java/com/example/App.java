package com.example;

import java.util.List;
import java.util.Map;
import java.util.zip.*;

import com.example.Services.SprintService;
import com.example.Services.TaskPlannerService;
import com.example.Services.TaskService;
import com.example.models.Sprint;
import com.example.models.SubTask;

import java.time.LocalDate;
import java.util.Date;
import com.example.models.SubTaskStatus;
import com.example.models.Task;
import com.example.models.TaskPlanner;
import com.example.models.TaskStatus;
import com.example.models.TaskType;

public class App {
    public static void main(String[] args) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);
        TaskPlanner taskPlanner = new TaskPlanner();
        TaskService taskService = new TaskService();
        TaskPlannerService taskPlannerService = new TaskPlannerService();
        SprintService sprintService = new SprintService();

        //Creating task
        Task task1 = taskService.createTask("Feature 1","User1",TaskType.FEATURE,TaskStatus.OPEN,new Date("12/28/2028"),"Assignee1");
        taskPlanner.getTasks().add(task1);
        Task task2 = taskService.createTask("Story 1","User1",TaskType.STORY,TaskStatus.OPEN,new Date(),"Assignee1");
        taskPlanner.getTasks().add(task2);
        Task task3 = taskService.createTask("Bug 1","User1",TaskType.BUG,TaskStatus.OPEN,new Date(),"Assignee1");
        taskPlanner.getTasks().add(task3);

        //Create and Add subtask
        SubTask subTask1 = taskService.createSubTask("Subtask 1", SubTaskStatus.OPEN, "Story 1");
        taskService.addSubTask(subTask1,task1);
        taskService.addSubTask(subTask1, task2);

        //Creating sprint
        Sprint sprint1 = sprintService.createSprint("sprint1",taskPlanner.getSprints());

        //Adding tasks to sprint
        sprintService.addTaskToSprint(sprint1, task1,taskPlanner.getSprints());
        sprintService.addTaskToSprint(sprint1, task3,taskPlanner.getSprints());
        sprintService.addTaskToSprint(sprint1, task2,taskPlanner.getSprints());
        sprintService.addTaskToSprint(sprint1, task2,taskPlanner.getSprints());

        taskPlannerService.displayUserTasks("Assignee1", taskPlanner.getTasks());

        taskPlannerService.displaySprintSnapshot(sprint1,taskPlanner.getSprints());
        
        //Transition of status
        System.out.println("Status allowed: "+taskService.isTransitionAllowed(task1,TaskStatus.DEPLOYED));


        //Change subtask status
        taskService.changeSubTaskStatus(SubTaskStatus.COMPLETED, subTask1);
        taskService.markTaskAsCompleted(task2);
        taskService.markTaskAsCompleted(task1);

        //Delete sprint
        sprintService.deleteSprint(sprint1, taskPlanner.getSprints());
        taskPlannerService.displaySprintSnapshot(sprint1,taskPlanner.getSprints());

    }
}
