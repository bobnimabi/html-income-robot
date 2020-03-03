package com.income.robot.service.dto;


import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Raddress {
  @JSONField(name = "OrderNo")
  private String OrderNo;
  
  @JSONField(name = "Contact")
  private String Contact;
  
  @JSONField(name = "OfficePhone")
  private String OfficePhone;
  
  @JSONField(name = "CellPhone")
  private String CellPhone;
  
  @JSONField(name = "State")
  private String State;
  
  @JSONField(name = "City")
  private String City;
  
  @JSONField(name = "District")
  private String District;
  
  @JSONField(name = "Address")
  private String Address;

}
