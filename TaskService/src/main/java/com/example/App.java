package com.example;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlInlineBinaryData;

import lombok.Builder;
import lombok.Data;



public class App {
    public static void main(String[] args) {
        TaskPlanner planner = new TaskPlanner();
        Task task1 = planner.createTask("Feature 1", "User1", TaskType.FEATURE, TaskStatus.OPEN, "2023-12-31", "Assignee1");
        SubTask subTask1 = planner.createSubTask("Subtask 1", SubTaskStatus.OPEN, "Story 1");
        task1.addSubTask(subTask1);

        Sprint sprint1 = planner.createSprint("Sprint 1");
        planner.addTaskToSprint(task1, sprint1);

         // Example of using allowed transitions
        Task featureTask = planner.createTask("Feature 1", "User1", TaskType.FEATURE, TaskStatus.OPEN, "2023-12-31", "Assignee1");
        featureTask.changeStatus(TaskStatus.IN_PROGRESS); // Allowed
        featureTask.changeStatus(TaskStatus.TESTING); // Allowed
        featureTask.changeStatus(TaskStatus.OPEN); // Not allowed, will not change


        System.out.println("User Tasks:");
        Map<TaskType, List<Task>> userTasks = planner.displayUserTasks("Assignee1");
        for (Map.Entry<TaskType, List<Task>> entry : userTasks.entrySet()) {
            System.out.println(entry.getKey() + " tasks: " + entry.getValue());
        }

        System.out.println("\nSprint Snapshot:");
        Map<String, List<Task>> sprintSnapshot = planner.displaySprintSnapshot(sprint1);
        for (Map.Entry<String, List<Task>> entry : sprintSnapshot.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
