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
import com.grupocastores.notificacion.dto.RegistrarTokenDTO;
import com.grupocastores.notificacion.dto.ResponseDTO;
import com.grupocastores.notificacion.service.INotificacionService;


@RestController
@RequestMapping(value ="/mensajes")
public class NotificacionController {
    
    @Autowired
    INotificacionService notificacionService;
    
    
    
    @PostMapping("/registrarDispositivo")
    public ResponseDTO registerDevice(@RequestBody RegistrarTokenDTO data) throws FileNotFoundException, FirebaseMessagingException {
        ResponseDTO responseDTO = notificacionService.registerDevice(data);
        return responseDTO;
        
    } 
    
    @PostMapping("/sendMulticast")
    public ResponseDTO notificacionViaje(@RequestBody List<String> tokens) throws FileNotFoundException, FirebaseMessagingException {
        
        if(tokens.isEmpty()) {
            return new ResponseDTO(0, "error", "No existen tokens", null);
        }
        ResponseDTO notification = notificacionService.sendMulticast(tokens);
        return notification;
        
    } 
    
    @GetMapping("/getToken/{idPersonal}")
    public ResponseDTO getToken(@PathVariable(name="idPersonal") int idPersonal) throws FileNotFoundException, FirebaseMessagingException {
        ResponseDTO token = notificacionService.getToken(idPersonal);
        return token;
        
    } 
    
    
      
}
