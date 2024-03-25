package com.bev0802.salesaccounting.wholesale.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Конфигурационный класс для настройки компонентов приложения.
 * Включает в себя настройку клиента RestTemplate с поддержкой балансировки нагрузки.
 */
@Configuration
public class AppConfig {
     /**
     * Создает и настраивает экземпляр RestTemplate для взаимодействия с внешними сервисами через REST API.
     * Аннотация {@link LoadBalanced} позволяет использовать RestTemplate в сочетании с балансировкой нагрузки,
     * автоматически подставляя подходящий инстанс сервиса при обращении к нему по имени.
     *
     * @return настроенный экземпляр RestTemplate.
     */
    @LoadBalanced
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

