package com.example.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;

@Data 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Sprint {
    private String name;
    @Default
    private List<Task> tasks = new ArrayList<>();
}