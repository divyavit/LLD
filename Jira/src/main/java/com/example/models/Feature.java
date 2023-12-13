package com.example.models;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Builder.Default;

@Data 
@AllArgsConstructor
@NoArgsConstructor
public class Feature extends Task {

    private String featureSummary;
    private ImpactType impact;
    
}
