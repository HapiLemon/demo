package xyz.hapilemon.spring.databinding.part4;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SteFourTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-mvc-four.xml");
        DependOnExoticType sample = (DependOnExoticType) context.getBean("sample");
        System.out.println(sample.getType().getName());
    }
}
