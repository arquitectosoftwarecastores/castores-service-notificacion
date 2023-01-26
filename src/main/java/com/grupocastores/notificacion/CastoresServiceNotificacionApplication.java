package com.grupocastores.notificacion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import com.lambdaworks.redis.*;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@EntityScan({ "com.grupocastores.commons.castoresdb", "com.grupocastores.commons.inhouse", "com.grupocastores.commons.oficinas", "com.grupocastores.notificacion.dto"})
public class CastoresServiceNotificacionApplication {

    public static void main(String[] args) {
        SpringApplication.run(CastoresServiceNotificacionApplication.class, args);
    }

}
