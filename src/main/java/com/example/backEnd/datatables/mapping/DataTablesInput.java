package com.example.backEnd.datatables.mapping;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTablesInput {

  @NotNull
  @Min(0)
  private Integer start = 0;

  @NotNull
  @Min(-1)
  private Integer length = 0;

  private List<Column> columns = new ArrayList<>();
}
