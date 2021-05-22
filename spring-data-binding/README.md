# Spring Data Binding

本demo体验了 Spring的PropertyEditor  
Property用于特定类型与字符串之间的转换，只在特定场景下有用

## Part 1

`DependOnExoticType`类中包含成员属性`ExoticType`  
在`spring-mvc-one.xml`中声明了一个bean 将`DependOnExoticType`放到容器中

```xml
<bean id="sample" class="org.example.databinding.part1.DependOnExoticType">
    <property name="type" value="aNameForExoticType"/>
</bean>
```

为`DependOnExoticType`.`type`赋值`"aNameForExoticType"`  
然后会自动调用`ExoticType`的有参构造函数 为`ExoticType`.`name`赋值

## Part 2

在Part 1的基础上，自定义PropertyEditor 需要继承`PropertyEditorSupport`类 重写`setAsText`方法  
可以将xml中配置的value赋值给`ExoticType`.`name`
---
但是需要遵守一定的规则  
`PropertyEditor`类和JavaBean在一个包内，并且他们`PropertyEditor`的类名是JavaBean的类名加上Editor

## Part 3

如果不想遵守上面的规则，则需要显示声明  
在xml文件中声明bean

```xml
<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
    <property name="customEditors">
        <map>
            <entry key="org.example.databinding.part3.ExoticType"
                   value="org.example.databinding.part3.ExoticTypeBinder"/>
        </map>
    </property>
</bean>
```

将`ExoticTypeBinder`映射为`ExoticType`的`PropertyEditor`

## Part 4

实现`PropertyEditorRegistrar`接口，注册自己定义的PropertyEditor

```java
public class CustomPropertyEditorRegistrar implements PropertyEditorRegistrar {
    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        // it is expected that new PropertyEditor instances are created
        registry.registerCustomEditor(ExoticType.class, new ExoticTypeBinder());

        // you could register as many custom property editors as are required here...
    }
}
```

```xml
<bean id="customPropertyEditorRegistrar" class="org.example.databinding.part4.CustomPropertyEditorRegistrar"/>

<bean class="org.springframework.beans.factory.config.CustomEditorConfigurer">
    <property name="propertyEditorRegistrars">
        <list>
            <ref bean="customPropertyEditorRegistrar"/>
        </list>
    </property>
</bean>
```

通过这种方式达到一次性注册多个自定义的`PropertyEditro`

## 参考资料

[Spring docs](https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-beans)  
[csdn参考demo](https://blog.csdn.net/u012345283/article/details/42169007)  
[在spring中使用自定义的PropertyEditor](https://my.oschina.net/thinwonton/blog/1492107)    
[spring源码分析-PropertyEditor](https://my.oschina.net/thinwonton/blog/1491733)