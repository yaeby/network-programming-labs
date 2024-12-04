package com.yaeby.spring_rabbitmq_test.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaftLeader {
    private int node;
    private int port;
}
