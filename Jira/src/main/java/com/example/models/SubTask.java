package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubTask {
    private String title;
    private Story story;
    private SubTaskStatus status;
    private String linkageWithStory;

}
