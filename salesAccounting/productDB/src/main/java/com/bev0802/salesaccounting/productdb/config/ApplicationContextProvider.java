package com.bev0802.salesaccounting.productdb.config;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Этот класс предоставляет статический способ доступа к контексту приложения Spring
 * и извлечения бинов из него. Он реализует {@link ApplicationContextAware}, чтобы
 * автоматически получать ссылку на {@link ApplicationContext}.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

    private static ApplicationContext context;
    /**
     * Устанавливает экземпляр {@link ApplicationContext}, который используется
     * этим классом для извлечения бинов.
     *
     * @param applicationContext экземпляр {@link ApplicationContext}, который будет установлен
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }

    /**
     * Извлекает бин из контекста приложения по его классу.
     *
     * @param <T> тип извлекаемого бина
     * @param beanClass класс извлекаемого бина
     * @return экземпляр бина указанного типа
     * @throws org.springframework.beans.BeansException если бин не может быть получен
     */
    public static <T> T getBean(Class<T> beanClass) {
        return context.getBean(beanClass);
    }
}

