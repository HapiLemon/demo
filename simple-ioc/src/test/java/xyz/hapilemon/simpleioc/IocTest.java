package xyz.hapilemon.simpleioc;

import org.junit.Test;
import xyz.hapilemon.simpleioc.context.ClassPathXmlApplicationContext;
import xyz.hapilemon.simpleioc.pojo.HelloWorldService;
import xyz.hapilemon.simpleioc.pojo.User;

public class IocTest {

    @Test
    public void testMyIoc() {

        ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("bean.xml");

        HelloWorldService helloWorldService = (HelloWorldService) classPathXmlApplicationContext.getBean("helloWorldService");
        helloWorldService.showHelloWorld();

        User user = (User) classPathXmlApplicationContext.getBean("user");
        user.showUser();

        System.out.println(helloWorldService);
        System.out.println(helloWorldService.user);
        System.out.println(helloWorldService.user.helloWorldService);
        System.out.println(helloWorldService.user.helloWorldService.user);
    }

}
