package com.example.models;

import java.util.List;

@Data 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskPlanner {
    private List<Task> tasks;
    private List<Sprint> sprints;



    public Sprint createSprint(String name) {
        Sprint sprint = new Sprint(name);
        sprints.add(sprint);
        return sprint;
    }

    public void deleteSprint(Sprint sprint) {
        sprints.remove(sprint);
    }

    public void addTaskToSprint(Task task, Sprint sprint) {
        sprint.addTask(task);
    }

    public void removeTaskFromSprint(Task task, Sprint sprint) {
        sprint.removeTask(task);
    }

    public Map<TaskType, List<Task>> displayUserTasks(String user) {
        Map<TaskType, List<Task>> userTasks = new HashMap<>();
        for (Task task : tasks) {
            if (user.equals(task.getAssignee())) {
                userTasks.computeIfAbsent(task.getType(), k -> new ArrayList<>()).add(task);
            }
        }
        return userTasks;
    }

    public Map<String, List<Task>> displaySprintSnapshot(Sprint sprint) {
        Map<String, List<Task>> snapshot = new HashMap<>();
        List<Task> delayed = new ArrayList<>();
        List<Task> onTrack = new ArrayList<>();

        for (Task task : sprint.getTasks()) {
            if (task.getDueDate() != null && !task.getDueDate().isEmpty() &&
                    !TaskStatus.COMPLETED.equals(task.getStatus())) {
                if (task.getDueDate().compareTo("currentDate") < 0) {
                    delayed.add(task);
                } else {
                    onTrack.add(task);
                }
            }
        }

        snapshot.put("Tasks", sprint.getTasks());
        snapshot.put("Delayed", delayed);
        snapshot.put("On Track", onTrack);

        return snapshot;
    }
}