### jf-helper
jf-helper is a simplified back-end development environment helper package for the jfinal framework.

The main uses are as follows:

jf-helper 是一个jfinal框架的简化搭建后端开发环境助手包。
主要使用方式如下：

1. 引入maven资源包
```
<dependency>
    <groupId>io.github.89ck</groupId>
    <artifactId>jf-helper</artifactId>
    <version>1.0</version>
</dependency>
jfinal 相关的资源包
```

2. 启动类继承 JfHelper类。
3. 数据库自动生成类 AbstractDbGenerater
4. 配置文件 application.yml。 
```yml
jFinal:
  dev-mode: true
  database:
    type: sqlserver
    url: jdbc:sqlserver://localhost:1433;databaseName=test
    driver-class: com.microsoft.sqlserver.jdbc.SQLServerDriver
    username: sa
    password: sa
```

可以使用 使用jfinal的注解配置路由。 @Path("/")
5. 启动项目。
```java
    JFinal.start("src/main/webapp", 5000, "/", 5);
```
6. 