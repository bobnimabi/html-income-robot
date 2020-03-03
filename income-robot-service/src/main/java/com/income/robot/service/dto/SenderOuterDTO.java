package com.income.robot.service.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class SenderOuterDTO {

  @JSONField(name = "Platform")
  private String Platform;
  
  @JSONField(name = "SendContact")
  private String SendContact;
  
  @JSONField(name = "SendOfficePhone")
  private String SendOfficePhone;
  
  @JSONField(name = "SendCellPhone")
  private String SendCellPhone;
  
  @JSONField(name = "SendState")
  private String SendState;
  
  @JSONField(name = "SendCity")
  private String SendCity;
  
  @JSONField(name = "SendDistrict")
  private String SendDistrict;
  
  @JSONField(name = "SendAddress")
  private String SendAddress;
  
  @JSONField(name = "ProductTitle")
  private String ProductTitle;
  
  @JSONField(name = "Weight")
  private String Weight;
  
  @JSONField(name = "Raddress")
  private Raddress[] Raddress;
}
