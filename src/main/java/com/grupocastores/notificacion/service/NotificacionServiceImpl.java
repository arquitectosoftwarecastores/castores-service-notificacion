package com.grupocastores.notificacion.service;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
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
import com.google.firebase.messaging.MulticastMessage;
import com.grupocastores.notificacion.dto.RegistrarToken;
import com.grupocastores.notificacion.dto.Response;
import com.lambdaworks.redis.RedisCommandExecutionException;
import com.lambdaworks.redis.RedisConnection;



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
    public Response sendMulticast(List<String> tokens)  {
        
        Response response = new Response();
        try {           
            Date date = new Date();
            String strDateFormat = "hh:mm:ss";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            String formattedDate= dateFormat.format(date);
            
            List<String> registrationTokens = Arrays.asList();
            
             tokens.forEach(item -> {
                 registrationTokens.add(item);
             });
            
            MulticastMessage message = MulticastMessage.builder()
                    .putData("message", "Se te asignó un nuevo viaje")
                    .putData("time", formattedDate)
                    .addAllTokens(registrationTokens)
                    .build();
            BatchResponse res = FirebaseMessaging.getInstance().sendMulticast(message);        
            response.setStatus(1);
            response.setDescriptionStatus("success");
            response.setDescription("Notificacion enviada correctamente");
            return response;
          
        } catch (FirebaseException e) {
           
            logger.error(e.getMessage());
            response.setStatus(0);
            response.setDescriptionStatus("error");
            response.setDescription("No fue posible enviar la notificacion");
            return response;
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
