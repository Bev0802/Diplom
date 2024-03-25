package com.bev0802.salesaccounting.wholesale.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

/**
 * Конфигурационный класс для настройки веб-слоя приложения.
 * Содержит настройки, необходимые для корректной работы веб-компонентов.
 */
@Configuration
public class WebConfig {
    /**
     * Создает и возвращает фильтр, который позволяет использовать HTTP-методы, такие как PUT, DELETE и PATCH
     * в веб-формах путем добавления скрытого поля с указанием желаемого метода. Это необходимо, поскольку
     * HTML формы нативно поддерживают только GET и POST методы.
     *
     * @return экземпляр {@link HiddenHttpMethodFilter}, настроенный для использования в приложении.
     */
    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}
