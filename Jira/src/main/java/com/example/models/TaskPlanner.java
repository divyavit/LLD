package com.example.models;

import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.Builder.Default;

@Data 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskPlanner {
    @Default
    private List<Task> tasks = new ArrayList<>();
    @Default
    private List<Sprint> sprints = new ArrayList<>();
}