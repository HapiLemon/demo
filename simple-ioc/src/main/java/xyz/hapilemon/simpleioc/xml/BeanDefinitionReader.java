package xyz.hapilemon.simpleioc.xml;

import xyz.hapilemon.simpleioc.resources.Resource;

public interface BeanDefinitionReader {
    void loadBeanDefinition(Resource resource);
}
