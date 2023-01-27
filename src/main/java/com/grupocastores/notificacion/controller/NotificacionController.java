package com.grupocastores.notificacion.controller;

import java.io.FileNotFoundException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.messaging.FirebaseMessagingException;
import com.grupocastores.notificacion.dto.RegistrarToken;
import com.grupocastores.notificacion.dto.Response;
import com.grupocastores.notificacion.service.INotificacionService;


@RestController
@RequestMapping(value ="/mensajes")
public class NotificacionController {
    
    @Autowired
    INotificacionService notificacionService;
    
    
    
    @PostMapping("/registrarDispositivo")
    public Response registerDevice(@RequestBody RegistrarToken data) throws FileNotFoundException, FirebaseMessagingException {
        Response response = notificacionService.registerDevice(data);
        return response;
        
    } 
    
    @PostMapping("/sendMulticast")
    public Response notificacionViaje(List<String> tokens) throws FileNotFoundException, FirebaseMessagingException {
        Response notification = notificacionService.sendMulticast(tokens);
        return null;
        
    } 
    
    @GetMapping("/getToken/{idPersonal}")
    public Response getToken(@PathVariable(name="idPersonal") int idPersonal) throws FileNotFoundException, FirebaseMessagingException {
        Response token = notificacionService.getToken(idPersonal);
        return token;
        
    } 
    
    
      
}
