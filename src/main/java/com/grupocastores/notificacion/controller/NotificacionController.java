package com.grupocastores.notificacion.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MulticastMessage;
import com.grupocastores.notificacion.dto.EnviarMensajeRequest;
import com.grupocastores.notificacion.dto.RegistrarToken;
import com.grupocastores.notificacion.dto.Response;
import com.grupocastores.notificacion.service.INotificacionService;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;


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
    
    @GetMapping("/notificacionInsertViaje/{idPersonal}")
    public Response notificacionViaje(@PathVariable(name="idPersonal") int idPersonal) throws FileNotFoundException, FirebaseMessagingException {
        Response notification = notificacionService.sendMulticast(idPersonal);
        return null;
        
    } 
    
    @GetMapping("/getToken/{idPersonal}")
    public Response getToken(@PathVariable(name="idPersonal") int idPersonal) throws FileNotFoundException, FirebaseMessagingException {
        Response token = notificacionService.getToken(idPersonal);
        return token;
        
    } 
    
    
      
}
