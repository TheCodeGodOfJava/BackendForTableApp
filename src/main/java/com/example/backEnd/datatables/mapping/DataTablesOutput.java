package com.example.backEnd.datatables.mapping;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTablesOutput<T> {

    private long recordsTotal = 0L;

    private List<T> data = new ArrayList<>();
}
