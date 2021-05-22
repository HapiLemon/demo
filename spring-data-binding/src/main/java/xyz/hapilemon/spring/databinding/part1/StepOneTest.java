package xyz.hapilemon.spring.databinding.part1;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StepOneTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-mvc-one.xml");
        DependOnExoticType sample = (DependOnExoticType) context.getBean("sample");
        System.out.println(sample.getType().getName());
    }
}
