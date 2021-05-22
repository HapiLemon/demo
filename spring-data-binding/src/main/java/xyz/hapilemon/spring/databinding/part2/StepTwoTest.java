package xyz.hapilemon.spring.databinding.part2;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class StepTwoTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("spring-mvc-two.xml");
        DependOnExoticType sample = (DependOnExoticType) context.getBean("sample");
        System.out.println(sample.getType().getName());

    }
}
