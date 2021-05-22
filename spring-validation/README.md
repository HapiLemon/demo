# 使用注解进行数据校验

## 0. 前言

使用注解进行参数校验的代码在annotation包里  
code包下的代码是实现validator进行校验

## 1. 简介

我们在实际开发中需要编写大量代码对数据进行校验，不断抛出异常，返回异常信息。但是 Spring 的 javax.validation 已经提供了一系列的注解，使用这些注解可以免去繁琐的 if-else 校验代码。

## 2. 快速上手

### 2.1 引入依赖

```xml
<dependencies>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
    <version>1.18.12</version>
  </dependency>
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
    <version>2.3.0.RELEASE</version>
  </dependency>
  <dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
    <version>6.0.9.Final</version>
  </dependency>
  <dependency>
    <groupId>javax.el</groupId>
    <artifactId>javax.el-api</artifactId>
    <version>3.0.0</version>
  </dependency>
  <dependency>
    <groupId>org.glassfish.web</groupId>
    <artifactId>javax.el</artifactId>
    <version>2.2.6</version>
  </dependency>
</dependencies>
```

### 2.2 体验一下

下面将展示对Controller层入参的校验

```java
@Data
public class Person {

    @NotNull(message = "classId 不能为空")
    private String classId;

    @Size(max = 33)
    @NotNull(message = "name 不能为空")
    private String name;

    @Pattern(regexp = "((^Man$|^Woman$|^UGM$))", message = "sex 值不在可选范围")
    @NotNull(message = "sex 不能为空")
    private String sex;

    @Email(message = "email 格式不正确")
    @NotNull(message = "email 不能为空")
    private String email;
}
```

```java
@PostMapping("/person")
public ResponseEntity<Person> create(@RequestBody @Valid Person person) {
    ...
}
```

当我传入一个不满足校验条件的 Person 对象时，将无法通过校验

## 3. 深度体验

> 下面将列举一些常见的使用情况

### 3.0 校验异常处理

#### 方法内处理异常

在需要校验的方法参数后面，紧跟**BindingResult**，就可以用**BindingResult**来接收校验的异常，而不抛出

```java
@PostMapping("/person")
public ResponseEntity<Person> create(@Valid Person person, BindingResult bindingResult) {
    System.out.println(bindingResult);
    return null;
}
```

#### 向上抛出异常

可以写一个异常处理中心，接收抛出的异常  
SpringMVC接口校验抛出MethodArgumentNotValidException.class异常  
非SpringMVC接口方法校验抛出ConstraintViolationException.class异常

```java
@RestControllerAdvice
public class ExceptionHandler {
    @org.springframework.web.bind.annotation.ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String constraintViolationExceptionHandler(ConstraintViolationException ex, HttpServletRequest request) {
        // 中间的异常处理根据自己的业务需求写
        ex.printStackTrace();
        System.out.println("-----------------ConstraintViolationException");
        return ex.getMessage();
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // 中间的异常处理根据自己的业务需求写
        ex.printStackTrace();
        System.out.println("-----------------MethodArgumentNotValidException");
        return ex.getMessage();
    }
}
```

### 3.1 对SpringMVC接口入参进行校验

#### 对@RequestBody 接收到的 DTO 进行校验

```java
@Data
public class Person {

    @NotNull(message = "classId 不能为空")
    private String classId;

    @Size(max = 33)
    @NotNull(message = "name 不能为空")
    private String name;

    @Pattern(regexp = "((^Man$|^Woman$|^UGM$))", message = "sex 值不在可选范围")
    @NotNull(message = "sex 不能为空")
    private String sex;

    @Email(message = "email 格式不正确")
    @NotNull(message = "email 不能为空")
    private String email;
}
```

```java
@PostMapping("/person")
public ResponseEntity<Person> create(@RequestBody @Valid Person person) {
    ...
}
```

当我传入一个不满足校验条件的 Person 对象时 （我传入了一个空的 Person 对象{}）抛出如下异常

```java
Validation failed for argument[0]in public org.springframework.http.ResponseEntity<io.ftlexpress.validate.annotation.Person>io.ftlexpress.validate.annotation.PersonController.create(io.ftlexpress.validate.annotation.Person)with 4 errors:[Field error in object'person'on field'name':rejected value[null];codes[NotNull.person.name,NotNull.name,NotNull.java.lang.String,NotNull];arguments[org.springframework.context.support.DefaultMessageSourceResolvable:codes[person.name,name];arguments[];default message[name]];default message[name 不能为空]][Field error in object'person'on field'classId':rejected value[null];codes[NotNull.person.classId,NotNull.classId,NotNull.java.lang.String,NotNull];arguments[org.springframework.context.support.DefaultMessageSourceResolvable:codes[person.classId,classId];arguments[];default message[classId]];default message[classId 不能为空]][Field error in object'person'on field'email':rejected value[null];codes[NotNull.person.email,NotNull.email,NotNull.java.lang.String,NotNull];arguments[org.springframework.context.support.DefaultMessageSourceResolvable:codes[person.email,email];arguments[];default message[email]];default message[email 不能为空]][Field error in object'person'on field'sex':rejected value[null];codes[NotNull.person.sex,NotNull.sex,NotNull.java.lang.String,NotNull];arguments[org.springframework.context.support.DefaultMessageSourceResolvable:codes[person.sex,sex];arguments[];default message[sex]];default message[sex 不能为空]]
```

#### 对@RequestBody 传入的集合类型的数据进行校验

```java
// 一定要在类上面加这个注解 生命这个bean需要进行校验
@Validated
public class PersonController {
	@PostMapping("/person")
    public ResponseEntity<Person> create(@RequestBody @Valid @NotEmpty Set<Person> persons) {
        ...															//  ↑ Set List 都可以
            // 参数前面需要加@Valid 来达到对集合的嵌套校验的目的 
            // 如果不加@NotEmpty 无法对List本身进行校验 当传入一个空的list时 也抛出异常
    }
}
```

- 空的List

![image.png](https://cdn.nlark.com/yuque/0/2020/png/2162474/1602580669687-03c8e731-2b09-473d-906d-5cf4e40ba163.png#align=left&display=inline&height=496&margin=%5Bobject%20Object%5D&name=image.png&originHeight=496&originWidth=1039&size=35537&status=done&style=none&width=1039)

- List中有一个空的Person

![image.png](https://cdn.nlark.com/yuque/0/2020/png/2162474/1602580685698-7488cda7-8ff3-4217-b37a-56026991b4cd.png#align=left&display=inline&height=455&margin=%5Bobject%20Object%5D&name=image.png&originHeight=455&originWidth=1493&size=46828&status=done&style=none&width=1493)
> 也有人通过实现List<> 用实现后的List<>接数据 可以不用写@NotEmpty
> 参考资料 [点我](https://blog.csdn.net/dxiaol/article/details/88718186)
> 但我认为，这么做实际上还是对一个对象进行嵌套校验。所以我选择加一个@NotEmpty 而不是实现List<>

#### 对@RequestParam传入的数据进行校验

```java
// 一定要在类上面加这个注解 生命这个bean需要进行校验
@Validated
public class PersonController {
    
	@PostMapping("/person")
    public ResponseEntity<Person> create(@RequestParam @NotBlank String name,@RequestParam @Max(100) Integer age) {
		...
    }
}
```

执行结果  
![image.png](https://cdn.nlark.com/yuque/0/2020/png/2162474/1602579745837-b7e19083-d1fb-42a3-a273-1294d3a2d958.png#align=left&display=inline&height=512&margin=%5Bobject%20Object%5D&name=image.png&originHeight=512&originWidth=888&size=38171&status=done&style=none&width=888)

### 3.2 对非SpringMVC接口的方法进行入参校验

```java
@Data
public class Person {

    @NotNull(message = "classId 不能为空")
    private String classId;

    @Size(max = 33)
    @NotNull(message = "name 不能为空")
    private String name;

    @Pattern(regexp = "((^Man$|^Woman$|^UGM$))", message = "sex 值不在可选范围")
    @NotNull(message = "sex 不能为空")
    private String sex;

    @Email(message = "email 格式不正确")
    @NotNull(message = "email 不能为空")
    private String email;
}
```

#### 当入参为对象时

```java
@Service
// 必须在类上添加@Validated注解 声明这个Bean需要进行参数校验
@Validated
public class PersonService {
    
    // 在这入参前面加上@Valid 进行嵌套校验
    public void create(@Valid Person person) {

    }
}
```

> 注意：此处如果我传入null 将通过校验。 所以需要添加@NotNull 在方法的参数前 进行空指针校验

```java
@GetMapping("/person")
public void create() {

    Person person = new Person();
    person.setEmail("123@sdfasdfasdf.com");
    person.setClassId("safd");
    person.setName("adfas");
    person.setSex("男");
	
    //此处调用上面的方法
    personService.create(person);
}
```

执行结果  
![image.png](https://cdn.nlark.com/yuque/0/2020/png/2162474/1602582229441-b9c07acf-6476-4962-b5f5-a1b07e5bd08b.png#align=left&display=inline&height=390&margin=%5Bobject%20Object%5D&name=image.png&originHeight=390&originWidth=757&size=28652&status=done&style=none&width=757)

#### 当入参为基本数据类型时

```java
@Service
// 必须在类上添加@Validated注解 声明这个Bean需要进行参数校验
@Validated
public class PersonService {

    public void create(@NotBlank String name, @Max(100) Integer age) {

    }
}

```

```java
@GetMapping("/person")
public void create() {
	// 调用上面的方法
    personService.create("",101);
}
```

执行结果  
![image.png](https://cdn.nlark.com/yuque/0/2020/png/2162474/1602582517181-09db8ca1-f25d-4f5a-a710-e843a7cf47f4.png#align=left&display=inline&height=409&margin=%5Bobject%20Object%5D&name=image.png&originHeight=409&originWidth=773&size=29656&status=done&style=none&width=773)

#### 当类是接口的实现类时

此时如果需要进行校验，则@Validated，@Valid等注解应该在接口中添加，而不在实现类里加。
> A method overriding another method must not redefine the parameter constraint configuration, but method PersonService#create(List) redefines the configuration of PersonServiceIF#create(List).

### 3.3 嵌套校验

当类中的成员变量是个对象时，可以对这个成员变量进行嵌套校验

```java
@Data
public class Person {

    @NotNull(message = "classId 不能为空")
    private String classId;

    @Size(max = 33)
    @NotNull(message = "name 不能为空")
    private String name;

    @Pattern(regexp = "((^Man$|^Woman$|^UGM$))", message = "sex 值不在可选范围")
    @NotNull(message = "sex 不能为空")
    private String sex;

    @Email(message = "email 格式不正确")
    @NotNull(message = "email 不能为空")
    private String email;

    @Valid
    @NotNull
    private List<Info> infoList;
    
    @Valid
    private Info info;

}

@Data
public class Info{

    @NotBlank
    private String name;

}
```

```java
@Service
// 必须在类上添加@Validated注解 声明这个Bean需要进行参数校验
@Validated
public class PersonService {

    public void create(@Valid @NotNull Person person) {

    }
}
```

```java
@GetMapping("/person")
    public void create() {

        Person person = new Person();
        person.setEmail("123@sdfasdfasdf.com");
        person.setClassId("safd");
        person.setName("adfas");
        person.setSex("男"); // 错误1

        Info info = new Info();
        info.setName(" "); // 错误2

        person.setInfo(info);
        person.setInfoList(Arrays.asList(info)); // 错误3
		
        // 调用上面的方法
        personService.create(person);
    }
```

执行结果  
![image.png](https://cdn.nlark.com/yuque/0/2020/png/2162474/1602582965732-b055dc6c-72a7-4e45-9e94-3a24035d0b07.png#align=left&display=inline&height=347&margin=%5Bobject%20Object%5D&name=image.png&originHeight=347&originWidth=1080&size=35485&status=done&style=none&width=1080)

### 3.4 分组校验

为约束添加groups

```java
@Data
public class Person {

    @NotNull(message = "classId 不能为空",groups = {StepOne.class})
    private String classId;

    @Size(max = 33)
    @NotNull(message = "name 不能为空",groups = {StepOne.class})
    private String name;

    @Pattern(regexp = "((^Man$|^Woman$|^UGM$))", message = "sex 值不在可选范围",groups = {StepTwo.class})
    @NotNull(message = "sex 不能为空",groups = {StepTwo.class})
    private String sex;

    @Email(message = "email 格式不正确",groups = {StepTwo.class})
    @NotNull(message = "email 不能为空",groups = {StepOne.class})
    private String email;

}
```

```java
@Service
// 必须在类上添加@Validated注解 声明这个Bean需要进行参数校验
@Validated
public class PersonService {

    @Validated({StepOne.class})
    public void create( @Valid @NotNull Person person) {

    }
}
```

```java
@GetMapping("/person")
public void create() {
    // 调用上面的方法
    personService.create(new Person());
}
```

执行结果  
![image.png](https://cdn.nlark.com/yuque/0/2020/png/2162474/1602583692704-2c92acad-8818-46fb-a371-3c9312e03067.png#align=left&display=inline&height=348&margin=%5Bobject%20Object%5D&name=image.png&originHeight=348&originWidth=1040&size=34570&status=done&style=none&width=1040)

#### 进阶使用 - 继承Default.class

如果上面的
> StepOne.class extend Default.class

那么在使用StepOne.class分组进行校验的时候，不仅会校验StepOne分组的字段，也会校验Default分组的字段

#### 进阶使用 - 分组校验顺序

```java
@GroupSequence({GroupA.class, GroupB.class, Default.class})
public interface GroupOrder {
}
```

在使用GroupOrder.class分组进行校验时，会按照GroupA->GroupB->Default的顺序校验

### 3.5 对方法的返回值进行校验

```java
@Service
// 必须在类上添加@Validated注解 声明这个Bean需要进行参数校验
@Validated
public class PersonService {

    @NotNull
    public String create() {
        return null;
    }
}
```

```java
    @GetMapping("/person")
    public void create() {
        personService.create();
    }
```

执行结果  
![image.png](https://cdn.nlark.com/yuque/0/2020/png/2162474/1602584010156-bb83bcb1-ddd7-4c63-ad78-e9fb8d401c53.png#align=left&display=inline&height=376&margin=%5Bobject%20Object%5D&name=image.png&originHeight=376&originWidth=625&size=25687&status=done&style=none&width=625)

### 3.6 配置快速失败返回模式

默认配置下，会校验所有的约束，而配置了快速失败返回模式后，只要校验到第一个不符合条件的字段就不继续校验而抛出异常。

```java
@Configuration
public class ValidatorConfiguration {
    @Bean
    public Validator validator(){
        ValidatorFactory validatorFactory = Validation.byProvider( HibernateValidator.class )
                .configure()
                .addProperty( "hibernate.validator.fail_fast", "true" )
                .buildValidatorFactory();
        Validator validator = validatorFactory.getValidator();

        return validator;
    }
}
```

## 4. 原理

### 4.1 SpringMVC接口的校验

之前我一直有个疑问，为什么Controller层的入参校验不需要在类前面加@Validated就能直接对入参进行嵌套校验

```java
package org.springframework.web.servlet.mvc.method.annotation;

public class 
    extends AbstractMessageConverterMethodProcessor {
 
    /**
	 * Throws MethodArgumentNotValidException if validation fails.
	 * @throws HttpMessageNotReadableException if {@link RequestBody#required()}
	 * is {@code true} and there is no body content or if there is no suitable
	 * converter to read the content with.
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, @Nullable ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest, @Nullable WebDataBinderFactory binderFactory) throws Exception {

		parameter = parameter.nestedIfOptional();
		Object arg = readWithMessageConverters(webRequest, parameter, parameter.getNestedGenericParameterType());
		String name = Conventions.getVariableNameForParameter(parameter);

		if (binderFactory != null) {
			WebDataBinder binder = binderFactory.createBinder(webRequest, arg, name);
			if (arg != null) {
                // 看这里-------------------------------------------------------------------
				validateIfApplicable(binder, parameter);
				if (binder.getBindingResult().hasErrors() && isBindExceptionRequired(binder, parameter)) {
					throw new MethodArgumentNotValidException(parameter, binder.getBindingResult());
				}
			}
			if (mavContainer != null) {
				mavContainer.addAttribute(BindingResult.MODEL_KEY_PREFIX + name, binder.getBindingResult());
			}
		}

		return adaptArgumentIfNecessary(arg, parameter);
	}
    
    protected void validateIfApplicable(WebDataBinder binder, MethodParameter parameter) {
		Annotation[] annotations = parameter.getParameterAnnotations();
		for (Annotation ann : annotations) {
			Validated validatedAnn = AnnotationUtils.getAnnotation(ann, Validated.class);
            // 这里找参数前的注解，如果存在以Valid开头的注解，就通过那个注解进行校验
			if (validatedAnn != null || ann.annotationType().getSimpleName().startsWith("Valid")) {
				Object hints = (validatedAnn != null ? validatedAnn.value() : AnnotationUtils.getValue(ann));
				Object[] validationHints = (hints instanceof Object[] ? (Object[]) hints : new Object[] {hints});
				binder.validate(validationHints);
				break;
			}
		}
	}
    
}
```

继而在 org.hibernate.validator.internal.engine.ValidatorImpl 中进行校验  
所以Controller层上面就不需要添加@Validated

### 4.2 非SpringMVC接口的校验

在每个参数前面声明约束注解的校验形式，就是方法级别的参数校验。  
实际上，这种形式可用于任何Spring
Bean的办法上，比方Controller/Service等。其底层实现原理就是AOP，具体来说是通过MethodValidationPostProcessor动静注册AOP切面，而后应用MethodValidationInterceptor对切点办法织入加强。

```java
public class MethodValidationPostProcessor extends AbstractBeanFactoryAwareAdvisingPostProcessorimplements InitializingBean {
    @Override
    public void afterPropertiesSet() {
        //为所有`@Validated`标注的Bean创立切面
        Pointcut pointcut = new AnnotationMatchingPointcut(this.validatedAnnotationType, true);
        //创立Advisor进行加强
        this.advisor = new DefaultPointcutAdvisor(pointcut, createMethodValidationAdvice(this.validator));
    }

    //创立Advice，实质就是一个方法拦截器
    protected Advice createMethodValidationAdvice(@Nullable Validator validator) {
        return (validator != null ? new MethodValidationInterceptor(validator) : new MethodValidationInterceptor());
    }
}
```

```java
public class MethodValidationInterceptor implements MethodInterceptor {
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        //无需加强的方法，间接跳过
        if (isFactoryBeanMetadataMethod(invocation.getMethod())) {
            return invocation.proceed();
        }
        //获取分组信息
        Class<?>[] groups = determineValidationGroups(invocation);
        ExecutableValidator execVal = this.validator.forExecutables();
        Method methodToValidate = invocation.getMethod();
        Set<ConstraintViolation<Object>> result;
        try {
            //方法入参校验，最终还是委托给Hibernate Validator来校验
            result = execVal.validateParameters(
                invocation.getThis(), methodToValidate, invocation.getArguments(), groups);
        }
        catch (IllegalArgumentException ex) {
            ...
        }
        //有异常便抛出
        if (!result.isEmpty()) {
            throw new ConstraintViolationException(result);
        }
        //真正的方法调用
        Object returnValue = invocation.proceed();
        //对返回值做校验，最终还是委托给Hibernate Validator来校验
        result = execVal.validateReturnValue(invocation.getThis(), methodToValidate, returnValue, groups);
        //有异常便抛出
        if (!result.isEmpty()) {
            throw new ConstraintViolationException(result);
        }
        return returnValue;
    }
}
```

## 5. 常用验证注解

![image.png](https://cdn.nlark.com/yuque/0/2020/png/2162474/1603184619099-8df3b4bf-e741-4a76-8be9-dbc3e809ce2d.png#align=left&display=inline&height=936&margin=%5Bobject%20Object%5D&name=image.png&originHeight=936&originWidth=1208&size=167010&status=done&style=none&width=1208)

## 6. 参考资料

[最佳实践1](https://www.cnblogs.com/mr-yang-localhost/p/7812038.html)  
[Spring Validation最佳实践与实现原理](https://zhuanlan.zhihu.com/p/205151764)  
[自定义Validated注解](https://juejin.im/post/6844903902811275278)  
[@ModelAttribute接收数据时的参数校验](https://www.coder.work/article/2801838)  
[Bean Validation完结篇：你必须关注的边边角角（约束级联、自定义约束、自定义校验器、国际化失败消息...）](https://blog.csdn.net/f641385712/article/details/97968775)  
[Spring方法级别数据校验：@Validated + MethodValidationPostProcessor优雅的完成数据校验动作](https://blog.csdn.net/f641385712/article/details/97402946)  
[解决@MultiRequestBody 无法使用@Validated](https://github.com/chujianyun/Spring-MultiRequestBody/issues/7)  
[@Valid 注解校验提交的List（list 集合） javax.validation.Valid](https://blog.csdn.net/dxiaol/article/details/88718186)  
[官方中文文档](https://docs.jboss.org/hibernate/validator/4.2/reference/zh-CN/html_single/#preface)  