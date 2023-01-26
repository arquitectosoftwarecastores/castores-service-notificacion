package com.grupocastores.notificacion.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.MulticastMessage;
import com.grupocastores.notificacion.dto.RegistrarToken;
import com.grupocastores.notificacion.dto.Response;
import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisCommandExecutionException;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisURI;

import io.lettuce.core.RedisConnectionException;

@Service
public class NotificacionServiceImpl implements INotificacionService{
    
    @Autowired
    RedisConn redisConnectionService;
    
    @Value("${app.firebase-configuration-file}")
    private String firebaseConfigPath;
    
    Logger logger = LoggerFactory.getLogger(NotificacionServiceImpl.class);

    
    /**
     * initializeFirebaseApp: Inicializa el la sesion para enviar notificaciones .
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return void
     * @throws try catch 
     * @date 2023-01-25
     */
    @Autowired
    public void initializeFirebaseApp() {
        try {
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(new ClassPathResource(firebaseConfigPath).getInputStream()))                 
                    .build();
            if (FirebaseApp.getApps().isEmpty()) {                
                FirebaseApp.initializeApp(options);
            }
        } catch (IOException e) {
            System.out.println(e);
            logger.error(e.getMessage());
        }        
    }
    
    /**
     * sendMulticast: Envia mensaje a diferentes tokens .
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return void
     * @throws Exception 
     * @date 2023-01-26
     */
    @Override
    public Response sendMulticast(int idPersonal)  {
        try {           
           
            List<String> registrationTokens = Arrays.asList(
                    "f5KVZeeWb8NcMvoX3nIuW:APA91bH78X465sy9S89zRG_kKmw_ML2k7gcsGsF46GhSEoL1wwB0xw3Gc9OLmannKp7ERMTZuVWvZcVpoUGixUxGS698JWo7bC30sQfqgD8Oh5udCvJ2YTmISanuRcutJJhm2Ti-kYbN");
            
            MulticastMessage message = MulticastMessage.builder()
                    .putData("message", "Se genero viaje con folio 2021000234")
                    .putData("time", "2:48885")
                    .addAllTokens(registrationTokens)
                    .build();
            BatchResponse response = FirebaseMessaging.getInstance().sendMulticast(message);        
            System.out.println(response.getResponses());
            
            return null;
          
        } catch (FirebaseException e) {
            System.out.println(e);
            logger.error(e.getMessage());
            return null;
        }
        
    }
    
    /**
     * connectToRedis:Hace conexion a redis .
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return void
     * @throws Exception 
     * @date 2023-01-25
     */
    @Override
    public Response getToken(int idPersonal) {
        Response response = new Response();
        try {
            RedisConnection<String, String> connection = redisConnectionService.getConnection();                  
            String value = connection.get("pol_operador_"+idPersonal); 
            
            response.setStatus(1);
            if(value != null) {          
                response.setDescriptionStatus("success");
                response.setDescription("Token obtenido correctamente");
                response.setValue(value);
            }else {               
                response.setDescriptionStatus("error");
                response.setDescription("No fue posible obtener el token");
                response.setValue(value);
            }
            return response;
        }catch (RedisCommandExecutionException e) {
            logger.error(e.getMessage());
            response.setStatus(0);
            response.setDescriptionStatus("error");
            response.setDescription("No fue posible obtener el token");
           
            return response;    
        } 
        catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatus(0);
            response.setDescriptionStatus("error");
            response.setDescription("No fue posible obtener el token");
           
            return response;     
        }
        
    }

    /**
     * registerDevice: registra un nuevo token en redis .
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Response
     * @throws Exception 
     * @date 2023-01-26
     */
    @Override
    public Response registerDevice(RegistrarToken data) {
        Response response = new Response();
        try {
            RedisConnection<String, String> connection = redisConnectionService.getConnection();
            connection.set("pol_inhouse_"+data.getIdPersonal(), data.getToken());
            
            response.setStatus(1);
            response.setDescriptionStatus("success");
            response.setDescription("Token registrado correctamente");
            
          return response;
          
        } catch (RedisCommandExecutionException e) {
            logger.error(e.getMessage());
            response.setStatus(0);
            response.setDescriptionStatus("error");
            response.setDescription("Error al registrar token");
            return response;
            
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            response.setStatus(0);
            response.setDescriptionStatus("error");
            response.setDescription("Error al registrar token");
            return response;
            
        }
        
    }

    
}
