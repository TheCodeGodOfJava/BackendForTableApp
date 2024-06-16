package com.example.backEnd.datatables.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTablesOutput<T> {

    private long recordsTotal = 0L;

    private List<T> data = new ArrayList<>();
}
