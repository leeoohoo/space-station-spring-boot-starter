# 开发空间站

## 简介
* 总结自己在开发过程中，遇到的一些问题，并将这些问题归集分析后， 得出的一些解决方案， 并开发成比较方便使用的注解；
* 本项目会持续更新，追加一些新的功能或优化完善已做好的功能；
* 如有更好的建议，或需要改进的部分，请各位大神能够多多指导；



## 目录
* 开始使用
* 验证器
* 事件驱动引擎
* 待开发内容
* 版本更新详情

## 使用方式
1. 引入maven
    ```
        <dependency>
            <groupId>io.github.leeoohoo</groupId>
            <artifactId>space-station-spring-boot-starter</artifactId>
            <version>0.0.1</version>
        </dependency>
    ```
2. 开启注解扫描 @EnableHappenProxy
    ```java
    /**
     * @author 17986
     */
    @SpringBootApplication
    @EnableHappenProxy
    @EnableFeignClients
    public class ScaffoldApplication {
    
        public static void main(String[] args) {
            SpringApplication.run(ScaffoldApplication.class, args);
        }
    
    }
    
    ```
3. 目前只有线程池bean 名的配置,在yml 中末尾添加即可
    ```
   station:
       task-executor-name: asyncTaskExecutor
   ```

## 验证器 @MyValidate 
该注解是基于 spring-boot-starter-validation 进行的二次封装，在原有的功能上，支持自己编写自己的验证逻辑，封装该注解的目的在于我发现很多验证的
逻辑会严重污染我们的正常的业务逻辑，于是尝试将数据验证的逻辑拆分出来，下面是代码示例：
```java
@Data
public class UserDto {

    @MyValidate(groupSize = 2,groupName = "nameAndIdCard",verify = NameAndIdCardValidate.class, message = "名字和身份证组合校验重复了")
    private String name;

    @MyValidate(groupSize = 2,groupName = "nameAndIdCard",verify = NameAndIdCardValidate.class, message = "名字和身份证组合校验重复了")
    private String idCard;

    @NotBlank(message = "不能为空")
    @MyValidate(verify = JobValidate.class, message = "工作的其他校验未通过")
    private String jobs;

    

    @MyValidate(verify = AgeValidator.class,message = "请输入年龄")
    private Integer age;




    @MyValidate(verify = DressValidate.class,message = "请输入地址")
    private String dress;


    public static class AgeValidator implements SingleValidate<Integer> {
        @Override
        public boolean validate(Integer obj) {
            if(obj>150 || obj <0) {
                return false;
            }
            return true;
        }
    }

    public static class DressValidate implements   SingleValidate<String>  {
        public boolean validate(String obj) {
            return true;
        }
    }


}

```
```java
@Verify
public class JobValidate implements SingleValidate<String> {
    private final UserService userService;

    public JobValidate(UserService userService) {
        this.userService = userService;
    }

    /**
     * 自己的校验逻辑
     * @param obj 是DTO中注解的字段的相应的值
     * @return
     */
    @Override
    public boolean validate(String obj) {
        System.out.println("测试");

        return true;
    }
}
```
```java
@Verify
public class NameAndIdCardValidate implements GroupValidate {

    /**
     * 
     * @param map key 就是字段的名称，value 就是值，在下面自己的逻辑中自取验证的字段的值进行组合验证
     * @return
     */
    @Override
    public boolean validate(Map<String, Object> map) {
        System.out.println(map);
        return true;
    }
}
```
使用时需要注意：
* 当需要注入其他服务的代码时，不能将验证器以内部类的方式写到DTO中，需要单独建立一个文件；
* 支持单个和分组校验，分组校验时需要额外标注分组名称和分组的size, 后期会尝试简化这部分内容；
* 可以在一个字段上添加多个注解，和原生的注解也可以组合使用

# 事件驱动引擎 @Happen @Trigger
* @Happen 标注到事件发生的方法上；
* @Trigger 标注到接收事件发生的方法上；

在开发过程中，有时会遇到如下几个场景可以使用本组注解:
* 在修改某一对象的状态值时，其他地方需要做相应的改变时；
* 对数据做流程是处理时；

以下为使用示例：
```java
/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/3
 */
@Service
@Slf4j
public class UserService {


    @Autowired
    private   UserRepository userRepository;





    @Happen("userSaveOrUpdate")
    @Transactional(rollbackFor = Exception.class)
    public String saveOrUpdate(UserDto userDto, String uuid){
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        userRepository.save(user);
        return "我返回的结果";
    }


    @Trigger(value = "validate",happenName = "userSaveOrUpdate",order = 1)
    @Transactional(rollbackFor = Exception.class)
    public String validatePassword(String result , UserDto userDto, String uuid){
        log.info("---------------------验证密码");
        // 如果此方法的入参没有遵循默认的（事件发生的返回值 ， 事件发生的入参顺序）入参顺序，
        // 则可以通过线程变量获取事件发生的返回值和入参的值
        Object resultThreadLocal = EventThreadValue.getResultThreadLocal();
        log.info(EventThreadValue.getParamsThreadLocal().toString());
        log.info("result:{}, user:{}, uuid:{}",result, userDto, uuid);
        if(null != resultThreadLocal) {
            log.info(resultThreadLocal.toString());
        }
        // 可以用取到的值进行其他操作
        User user = new User();
        BeanUtils.copyProperties(userDto,user);
        userRepository.save(user);
        return "测试";
    }


    @Trigger(value = "test",happenName = "userSaveOrUpdate",order = 2)
    @Transactional(rollbackFor = Exception.class)
    public void test( String result ,UserDto user,String uuid){
        Object validate = EventThreadValue.getStepResultThreadLocal("validate");
        log.info("我是测试---------------------");
        Object resultThreadLocal = EventThreadValue.getResultThreadLocal();
        log.info(EventThreadValue.getParamsThreadLocal().toString());
        log.info("result:{}, user:{}, uuid:{}",result, user, uuid);
        if(null != resultThreadLocal) {
            log.info(resultThreadLocal.toString());
        }
        log.info("我是测试---------------------");

    }

    @Trigger(value = "boo",happenName = "boo")
    public void booTrigger() {
        System.out.println("我走到boo");
    }
}

```
```java
/**
 * @Description:
 * @Author: leeoohoo@gmail.com
 * @CreateTime: 2022/8/8
 */
@Slf4j
@Service
public class TriggerTest {
    private final  UserRepository repository;

    public TriggerTest(UserRepository repository) {
        this.repository = repository;
    }


    @Trigger(value = "test",happenName = "userSaveOrUpdate",async = true)
    @Happen("boo")
    public void test(String result , UserDto user, String uuid) throws InterruptedException {
        User user1 = new User();
        BeanUtils.copyProperties(user,user1);
        repository.save(user1);
        log.info("我是另一个测试---------------------");
        Thread.sleep(5000);
        log.info("我睡了一会");
        Object resultThreadLocal = EventThreadValue.getResultThreadLocal();
        log.info(EventThreadValue.getParamsThreadLocal().toString());
        log.info("result:{}, user:{}, uuid:{}",result, user,uuid);
        if(null != resultThreadLocal) {
            log.info(resultThreadLocal.toString());
        }
    }
}

```
实现RecordAbstract接口,重写可以拿到事件发生器与事件接收器执行过程中的记录
```java

public class TestRecord implements RecordAbstract {
    @Override
    public void saveTrigger(CirculationRecord circulationRecord) {
        System.out.println("测试是否可以拿到数据");
        System.out.println(circulationRecord.toString());
    }

    @Override
    public void saveHappen(CirculationRecord circulationRecord) {
        System.out.println("测试是否可以拿到数据");
        System.out.println(circulationRecord.toString());
    }
}
```
别忘了通过@Bean注入到ioc中
```
    @Bean
	public RecordAbstract getRecordAbstract() {
		return new TestRecord();
	}

```

使用注意：
* 事件接收器的方法的类确保被spring IOC管理，且暂不支持Interface的方式
* 事件接收器的方法入参推荐顺序为 “事件发生方法的返回参数”， ”事件发生方法的入参顺序"，这样的话注解会自动将事件发生器的入参赋值；
* 事件接收器同时也可以成为事件发生源，例如
    ```
    
        @Trigger(value = "test",happenName = "value",async = true)
        @Happen("boo")
        public void test(String result , UserDto user, String uuid) throws InterruptedException {
            User user1 = new User();
            BeanUtils.copyProperties(user,user1);
            repository.save(user1);
            log.info("我是另一个测试---------------------");
            Thread.sleep(5000);
            log.info("我睡了一会");
            Object resultThreadLocal = EventThreadValue.getResultThreadLocal();
            log.info(EventThreadValue.getParamsThreadLocal().toString());
            log.info("result:{}, user:{}, uuid:{}",result, user,uuid);
            if(null != resultThreadLocal) {
                log.info(resultThreadLocal.toString());
            }
        }
    ```
    以上片段作为事件的接收者，接收userSaveOrUpdate的事件的同时，又发起了 "boo"的事件。
    ```
        @Trigger(value = "boo",happenName = "boo")
        public void booTrigger() {
            System.out.println("我走到boo");
        }
    ```
    由以上方法接收，入参可以遵从上文说过的顺序，也可以没有参数，从线程变量中获取。
* 事件接收器支持事物（TransactionDefinition.PROPAGATION_REQUIRES_NEW），目前仅支持开启新的事物，后续可以继续跟@Transactional做深入兼容；
* 如事件接收器是异步的情况下，可以在配置文件中配置自己的线程池的名称，如未配置则默认不使用线程池；
* 异步情况下，忽略事物，同时会在所有同步事件接收器执行完毕以后，开始执行所有异步执行器;
* 事件接收器会根据配置 "order" 自然排序顺序执行
* EventThreadValue.getStepResultThreadLocal("validate") 可以根据线程变量获取已经执行过的事件接收器的返回参数

# 待开发内容
1. 完善事件驱动引擎，开放若干接口供用户实现；
2. 基于DTO添加注解，自动生成 select 语句与where 语句；

# 版本更新
## 0.0.1 
为初始版本，包含验证器与事件驱动器两组注解功能
## 0.0.2 
更新事件驱动器注解，添加RecordAbstract的实现功能，可以自定义将事件发生的过程参数记录下来
@Happen 注解添加新属性 boolean enabledSave() default false; 默认不记录





