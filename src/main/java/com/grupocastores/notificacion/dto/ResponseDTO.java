package com.grupocastores.notificacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {

  private int status;
  private String descriptionStatus;
  private String description;
  private String value;

}
