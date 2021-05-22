package xyz.hapilemon.simpleioc.facotry;

import xyz.hapilemon.simpleioc.BeanDefinition;

public interface BeanFactory {

    Object getBean(String name);

    void registerBeanDefinition(String name, BeanDefinition beanDefinition);
}
