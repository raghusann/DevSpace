package com.devspace.database.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SqlQueryResponse {

    private List<String> columns;
    private List<Map<String, Object>> rows;
    private int rowCount;
    private String message;
}
