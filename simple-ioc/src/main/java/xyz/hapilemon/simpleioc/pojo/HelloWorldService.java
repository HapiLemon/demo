package xyz.hapilemon.simpleioc.pojo;

public class HelloWorldService {
    // 产生依赖循环
    public User user;
    private String name;
    private Integer age;

    public void showHelloWorld() {
        System.out.println(name + ":" + age);
    }
}
