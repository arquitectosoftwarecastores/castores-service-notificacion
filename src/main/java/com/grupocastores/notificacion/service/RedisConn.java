package com.grupocastores.notificacion.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.lambdaworks.redis.RedisClient;
import com.lambdaworks.redis.RedisConnection;
import com.lambdaworks.redis.RedisConnectionException;
import com.lambdaworks.redis.RedisURI;


@Component
public class RedisConn {
    
    protected RedisClient redisClient;
    public RedisConnection<String, String> connection;
    
    
    /**
     * RedisConn: conexion a redis .
     * 
    
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return void
     * @throws Exception 
     * @date 2023-01-25
     */
    @Autowired
    public RedisConn() {
        try {       
            this.redisClient = new RedisClient(
                    RedisURI.create("redis://10.1.9.73:6379")); 
            this.connection = this.redisClient.connect();
          
        } catch (RedisConnectionException e) {
            System.out.println(e.getMessage());
           
        }
    }
    

    public RedisConnection<String, String> getConnection() {
        return connection;
    }


    public void setConnection(RedisConnection<String, String> connection) {
        this.connection = connection;
    }

    public void closeConnection() {
        connection.close();
        redisClient.shutdown();
    }
   
}
