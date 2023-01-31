package com.grupocastores.notificacion.service;

import java.util.List;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.grupocastores.notificacion.dto.RegistrarTokenDTO;
import com.grupocastores.notificacion.dto.ResponseDTO;


public interface INotificacionService {


    /**
     * sendMulticast: Envia mensaje a diferentes tokens .
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return void
     * @throws ResponseDTO 
     * @date 2023-01-26
     */
    public ResponseDTO sendMulticast(List<String> tokens);
    
    /**
     * connectToRedis:Hace conexion a redis .
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return String
     * @throws Exception 
     * @date 2023-01-25
     */
    public ResponseDTO getToken(int idPersonal);
    
    /**
     * registerDevice: registra un nuevo token en redis
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Response
     * @throws Exception 
     * @date 2023-01-26
     */
    public ResponseDTO registerDevice(RegistrarTokenDTO data);

}
