package com.example.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data 
@Builder
@AllArgsConstructor
@NoArgsConstructor
class SubTask {
    private String title;
    private SubTaskStatus status;
    private String linkageWithStory;

}
