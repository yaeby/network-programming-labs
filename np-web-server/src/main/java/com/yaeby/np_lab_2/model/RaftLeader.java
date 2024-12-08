package com.yaeby.np_lab_2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RaftLeader {
    private int node;
    private String host;
    private int port;
}