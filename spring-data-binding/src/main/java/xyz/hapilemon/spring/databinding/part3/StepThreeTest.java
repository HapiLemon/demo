package xyz.hapilemon.spring.databinding.part3;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StepThreeTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-mvc-three.xml");
        DependOnExoticType sample = (DependOnExoticType) context.getBean("sample");
        System.out.println(sample.getType().getName());
    }
}
