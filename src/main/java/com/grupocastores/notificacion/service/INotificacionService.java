package com.grupocastores.notificacion.service;

import java.util.List;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.grupocastores.notificacion.dto.RegistrarToken;
import com.grupocastores.notificacion.dto.Response;


public interface INotificacionService {


    /**
     * sendMulticast: Envia mensaje a diferentes tokens .
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return void
     * @throws Exception 
     * @date 2023-01-26
     */
    public Boolean sendMulticast();
    
    /**
     * connectToRedis:Hace conexion a redis .
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return void
     * @throws Exception 
     * @date 2023-01-25
     */
    public void getToken();
    
    /**
     * registerDevice conexion a redis .
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return void
     * @throws Exception 
     * @date 2023-01-26
     */
    public Response registerDevice(RegistrarToken data);

}
