package com.example.models;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Builder.Default;

@Data 
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Sprint {
    private String name;
    @Default
    private List<Task> stories = new ArrayList<>();
}