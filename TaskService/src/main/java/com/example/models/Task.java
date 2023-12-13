package com.example.models;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data 
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Task {
    private String title;
    private String creator;
    private String assignee;
    private TaskStatus status;
    private TaskType type;
    private String dueDate;
    @Default
    private List<SubTask> subtasks = new ArrayList<>();

       public boolean isTransitionAllowed(TaskStatus newStatus) {
        Map<TaskStatus, Set<TaskStatus>> allowedTransitions = getAllowedTransitions();
        Set<TaskStatus> allowedNextStatus = allowedTransitions.getOrDefault(this.status, Collections.emptySet());
        return allowedNextStatus.contains(newStatus);
    }

    private Map<TaskStatus, Set<TaskStatus>> getAllowedTransitions() {
        Map<TaskStatus, Set<TaskStatus>> allowedTransitions = new HashMap<>();

        // Define allowed transitions for FEATURE tasks
        if (TaskType.FEATURE.equals(this.type)) {
            allowedTransitions.put(TaskStatus.OPEN, new HashSet<>(Arrays.asList(TaskStatus.IN_PROGRESS)));
            allowedTransitions.put(TaskStatus.IN_PROGRESS, new HashSet<>(Arrays.asList(TaskStatus.TESTING, TaskStatus.DEPLOYED)));
            allowedTransitions.put(TaskStatus.TESTING, new HashSet<>(Arrays.asList(TaskStatus.DEPLOYED)));
        }
        // Add similar definitions for other task types (BUG, STORY)

        return allowedTransitions;
    }

    public void changeStatus(TaskStatus newStatus) {
        // Implement logic for allowed transitions based on task type
        // ...

        this.status = newStatus;
    }

    public void changeAssignee(String newAssignee) {
        this.assignee = newAssignee;
    }

    public void addSubTask(SubTask subtask) {
        subtasks.add(subtask);
    }
}
