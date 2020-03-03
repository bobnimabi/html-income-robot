package com.income.robot.service.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@NoArgsConstructor
@AllArgsConstructor
@Data
public class GoodsWaitDTO {
  private String loginName;
  
  private String shopId;
  
  private String orderId;
  
  private BigDecimal orderPrice;
  
  private Integer orderType;
  
  private String platform;
  
  private String sendContact;
  
  private String sendState;
  
  private String sendCity;
  
  private String sendDistrict;
  
  private String contact;
  
  private String state;
  
  private String city;
  
  private String district;
  
  private String address;
  

}
