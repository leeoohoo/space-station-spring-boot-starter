# 开发空间站

## 简介
* 总结自己在开发过程中，遇到的一些问题，并将这些问题归集分析后， 得出的一些解决方案， 并开发成比较方便使用的注解；
* 配合本项目还有另一个Demo 项目，可以结合阅读；
* 本项目会持续更新，追加一些新的功能或优化完善已做好的功能；


## 目录
* 验证器
* 事件驱动引擎

### 验证器 @MyValidate 
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


    public static class AgeValidator implements SingleValidate {
        @Override
        public boolean validate(Object obj) {
            System.out.println(obj);
            System.out.println("我接受到什么");

            return true;
        }
    }

    public static class DressValidate implements   SingleValidate  {
        public boolean validate(Object obj) {
            return true;
        }
    }


}

```
```java
@Verify
public class JobValidate implements SingleValidate {
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
    public boolean validate(Object obj) {
        System.out.println("测试");

        return true;
    }
}
```
```java
@Verify
public class NameAndIdCardValidate implements GroupValidate {

    @Override
    public boolean validate(Map<String, Object> map) {
        System.out.println(map);
        return true;
    }
}
```
使用时需要注意的是，当需要注入其他服务的代码时，不能将验证器以内部类的方式写到DTO中，

