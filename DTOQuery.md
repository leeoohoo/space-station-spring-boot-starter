# 目录
* 前言
* 对比
* 使用方法
* 前景展望


# 前言
第一次发表这么正式的博客，内心还是有点忐忑。首先感谢正在阅读本文的大家，如有写的不对的地方，还请大家海涵，如能不吝赐教那就更加感谢了。

本博文主要是想介绍一下本人开发的“DTOQuery”一款ORM框架，其实不能说是ORM框架，主要还是站在现在主流的ORM框架的基础上进行开发，主要目的是减少学习曲线，并加速开发的效率。当然以上也只是我自己觉得，是否真的达到了这样的效果，或者说在未来能否能达到这样的效果，还要请各位多多的赐教了，这里我先抛砖引玉。

# 对比
我们主要对比的是现目前使用比较多的两款主流的ORM 框架，一个是MyBatis Plus，还有一个是JPA。

首先我要声明的是这里的对比并不是比一个高低，只是将我在工作中日常看到的代码拉出来看一下，本人也是站在这两款ORM框架的肩膀上进行开发的。我也是基于工作中这些现象，才决定开发一款这样的一款工具包来简化工作中的一些不必要的代码。

那下面我直接上图
![](https://oscimg.oschina.net/oscnet/up-57802cbd3e6a05a39cf7f71632b3296b86c.png)
当我们用Mybatis Plus实现一些相对复杂的sql时，就需要自己到XML中自己写一下sql了，并且像一些可以为空的条件就要去写一些if 标签，其实这种代码多了，属实也是挺崩溃的。
![](https://oscimg.oschina.net/oscnet/up-45efdf312dc9ca3ead95e9e37446bd30bad.png)
![](https://oscimg.oschina.net/oscnet/up-e34aaf7509d5105bff9a76aa699fd6c2066.png)
以上是在java 文件里加的一些判断，看着也是非常的冗长和麻烦，我们在开发一个简单的多条件查询的时候，尽然还要这么费事费时吗，有没有办法简化来提高我们效率，答案是肯定的。

以上我们看到是Mybatis Plus的个例，下面我们来看看JPA的
![](https://oscimg.oschina.net/oscnet/up-648cf689af3558907d4827d0dbc1959c4e9.png)

```java
public class Test {
    @RequestMapping("/user/list.do")
    public  ResponseBean getUserList(){
        QSysUser qsysuser = QSysUser.sysUser;
        QSysRole qsysrole = QSysRole.sysRole;
        QSysUserRole quserrole = QSysUserRole.sysUserRole;

        QBean<ItemsBean> bean = Projections.bean(
                ItemsBean.class,
                qsysuser.id,
                qsysuser.username,
                qsysuser.pwd,
                qsysrole.title,
                quserrole.roleId
        );

        // 左联三张表 并对id进行排序
        List list =  jpaQueryFactory.select(bean).from(qsysuser)
                .leftJoin(quserrole).on(qsysuser.id.eq(quserrole.userId))
                .leftJoin(qsysrole).on(quserrole.roleId.eq(qsysrole.id))
                .orderBy(qsysuser.id.asc()).fetch();

        return new ResponseBean(200,null,list);
    }
}

```

以上是JPA 的示例代码，可以看出在处理多条件查询的时候，几乎是差不多的处理手法，在做表连接时也是相当的麻烦。

为了提高开发的效率，将这些琐碎的参数处理，链表处理更方便，更易懂的方式展现出来，于是我开发了DTOQuery。

# 使用方法
终于进入了主题，我上面说了一大堆，那么到底这个该怎么用呢？接下来我将详细的将给大家听，同时我将自己基于DTOQuery 来实现一个个人主页的项目来进一步测试，后续代码也会公开在github 上以供参考。

首先分享一下源码的地址和maven 中央库的地址

maven: https://search.maven.org/artifact/io.github.leeoohoo/space-station-spring-boot-starter/0.0.3/jar

github: https://github.com/leeoohoo/space-station-spring-boot-starter

注意DTOQuery 在第三个版本后才支持的，前两个版本做的内容在github 上有详细的描述，有感兴趣的伙伴也可以看看。

查询主要分为两种方式，一种的通过在DTO上添加注解，最终生成sql，最终交给jpa 进行执行，还有一种是链式调用，主要针对相对复杂的查询环境，支持常用的SQL 函数。这里我稍微插一句题外话，我认为随着微服务的发展，我们逐渐的将较为复杂的业务分为多个服务去执行，所以在数据库层也慢慢的简化，所以sql 大部分情况下就不会那么的复杂，而DTOQuery就是为了那些大部分不是特别复杂的SQL而生。

* ## DTO模型查询
当我们有一天看到我们的DTO 就知道这个接口的查询逻辑，想要加一个搜索条件或减少一个搜索条件也只要加减个字段，那该多好啊，甚至可以定义一些必填的搜索条件等等，是不是听下来还不错？那不多废话了，直接上代码。