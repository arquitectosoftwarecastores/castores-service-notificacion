package com.grupocastores.notificacion.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnviarMensajeRequest {
    private int idPersonal;
    private String token;
    private int idUnidad;
}
