package com.ingeniods.scheduler.api.dto.response;

import com.ingeniods.scheduler.domain.type.Status;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TaskResponse {

    private String id;
    private Status status;
    
}