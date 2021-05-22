package xyz.hapilemon.simpleioc.pojo;

public class User {

    // 产生依赖循环
    public HelloWorldService helloWorldService;
    private String name;

    public void showUser() {
        System.out.println(name);
    }
}