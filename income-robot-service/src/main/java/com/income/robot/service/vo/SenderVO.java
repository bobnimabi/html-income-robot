package com.income.robot.service.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SenderVO {
  private String code;
  
  private String msg;
  
  private SenderInnerVO[] Data;
}
