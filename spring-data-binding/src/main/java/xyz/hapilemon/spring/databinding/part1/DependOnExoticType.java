package xyz.hapilemon.spring.databinding.part1;

public class DependOnExoticType {

    /**
     * spring-mvc-one.xml 为type赋值 aNameForExoticType
     * 然后 {@link ExoticType}有参构造
     * 入参为String自动转为了ExoticType
     */
    private ExoticType type;

    public ExoticType getType() {
        return type;
    }

    public void setType(ExoticType type) {
        this.type = type;
    }
}
