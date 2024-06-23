package com.example.backEnd.datatables.mapping;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Column {

  private String orderDirection;

  private String search;

  private String alias;
}
