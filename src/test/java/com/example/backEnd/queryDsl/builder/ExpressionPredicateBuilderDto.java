package com.example.backEnd.queryDsl.builder;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ExpressionPredicateBuilderDto {

  private Long numberField;

  private String stringField;
}
