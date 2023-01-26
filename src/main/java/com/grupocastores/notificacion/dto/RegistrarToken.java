package com.grupocastores.notificacion.dto;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name="registrarTokes")
public class RegistrarToken {
    @Id
    private int idPersonal;
    private String token;
}
