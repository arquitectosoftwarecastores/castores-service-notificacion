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
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.AndroidNotification;
import com.google.firebase.messaging.AndroidNotification.Visibility;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.Aps;
import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.MulticastMessage;
import com.google.firebase.messaging.Notification;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushFcmOptions;
import com.google.firebase.messaging.WebpushNotification;
import com.grupocastores.notificacion.dto.RegistrarTokenDTO;
import com.grupocastores.notificacion.dto.ResponseDTO;
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
    public ResponseDTO sendMulticast(List<String> tokens)  {
        
        ResponseDTO responseDTO = new ResponseDTO();
        try {           
            Date date = new Date();
            String strDateFormat = "hh:mm:ss";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            String formattedDate= dateFormat.format(date);
            
            
            MulticastMessage message = MulticastMessage.builder()
                        .putData("time", formattedDate)
                        .setNotification(Notification.builder()
                            .setTitle("Viaje asignado")
                            .setBody("Se te ha asignado un nuevo viaje")
                            .build())
                        .setAndroidConfig(AndroidConfig.builder()
                            .setTtl(3600 * 1000)
                            .setNotification(AndroidNotification.builder()
                                .setIcon("https://www.google.com/images/branding/googlelogo/2x/googlelogo_color_272x92dp.png")
                                .setColor("#f45342")
                                .setVisibility(Visibility.PUBLIC)
                                .setClickAction("click_action")
                                .build())
                            .build())
                        .setApnsConfig(ApnsConfig.builder()
                            .setAps(Aps.builder()
                                    .setCategory("INVITE_CATEGORY")
                                .setBadge(42)
                                .build())
                            .build())
                        .setWebpushConfig(WebpushConfig.builder()
                                .setNotification(WebpushNotification.builder().putCustomData("requireInteraction", true).build())
                                .setFcmOptions(WebpushFcmOptions.withLink("/"))
                                .build())
                        .addAllTokens(tokens)
                        .build();
            BatchResponse res = FirebaseMessaging.getInstance().sendMulticast(message); 
            System.out.println(res.getFailureCount());
            System.out.println(res.getSuccessCount() + " messages were sent successfully");
            responseDTO.setStatus(1);
            responseDTO.setDescriptionStatus("success");
            responseDTO.setDescription("Notificacion enviada correctamente");
            return responseDTO;
          
        } catch (FirebaseException e) {
           
            logger.error(e.getMessage());
            responseDTO.setStatus(0);
            responseDTO.setDescriptionStatus("error");
            responseDTO.setDescription("No fue posible enviar la notificacion");
            return responseDTO;
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
    public ResponseDTO getToken(int idPersonal) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            RedisConnection<String, String> connection = redisConnectionService.getConnection();                  
            String value = connection.get("pol_operador_"+idPersonal); 
            
            responseDTO.setStatus(1);
            if(value != null) {          
                responseDTO.setDescriptionStatus("success");
                responseDTO.setDescription("Token obtenido correctamente");
                responseDTO.setValue(value);
            }else {               
                responseDTO.setDescriptionStatus("error");
                responseDTO.setDescription("No fue posible obtener el token");
                responseDTO.setValue(value);
            }
            return responseDTO;
        }catch (RedisCommandExecutionException e) {
            logger.error(e.getMessage());
            responseDTO.setStatus(0);
            responseDTO.setDescriptionStatus("error");
            responseDTO.setDescription("No fue posible obtener el token");
           
            return responseDTO;    
        } 
        catch (Exception e) {
            logger.error(e.getMessage());
            responseDTO.setStatus(0);
            responseDTO.setDescriptionStatus("error");
            responseDTO.setDescription("No fue posible obtener el token");
           
            return responseDTO;     
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
    public ResponseDTO registerDevice(RegistrarTokenDTO data) {
        ResponseDTO responseDTO = new ResponseDTO();
        try {
            RedisConnection<String, String> connection = redisConnectionService.getConnection();
            connection.set("pol_inhouse_"+data.getIdPersonal(), data.getToken());
            
            responseDTO.setStatus(1);
            responseDTO.setDescriptionStatus("success");
            responseDTO.setDescription("Token registrado correctamente");
            
          return responseDTO;
          
        } catch (RedisCommandExecutionException e) {
            logger.error(e.getMessage());
            responseDTO.setStatus(0);
            responseDTO.setDescriptionStatus("error");
            responseDTO.setDescription("Error al registrar token");
            return responseDTO;
            
        }
        catch (Exception e) {
            logger.error(e.getMessage());
            responseDTO.setStatus(0);
            responseDTO.setDescriptionStatus("error");
            responseDTO.setDescription("Error al registrar token");
            return responseDTO;
            
        }
        
    }

    
}
