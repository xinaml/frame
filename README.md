# frame
``工具：idea``

``环境：fedora27+jdk1.8``

``框架基础：SpringBoot2.0.1、Jpa、hibernate5.2、Gradle4+、Mysql5+``

``页面渲染：thymeleaf``

``json插件: fastJson``

``日志：log4j2``

``连接池：druid``

``api : swagger``

``目录结构：``


        1-- base    所有基类包，及基类封装的类包
        2-- action  控制器层类包
        3-- dto     查询对象传输层类包
        4-- to      数据传输层类包
        5-- entity  实体类类包
        6-- rep     jpa接口映射类包
        7-- ser     业务层类包
        8-- types   枚举类型类包
        9--common   通用包
                9.1-- aspect    切面处理类包                     
                9.2-- custom    自定义类包
                      9.2.1 -- annotation：自定义注解包
                      9.2.2 -- exception ： 自定义异常包
                      9.2.3 -- result ： 自定义返回数据类包
                      9.2.4 -- types ： 共用枚举类型类包
                9.3-- config    配置类包
                9.4-- handler   处理器类包
                9.5-- utils     通用调工具类包
                9.6-- interceptor     拦截器处理类包
        10 --AppRoot 启动类
         
        11--resources
            11.1-- static                 静态文件，js,css等
            11.2-- templates              页面
            11.3-- application.properties 主配置文件
            11.4-- config.properties      配置文件
            11.5-- logback.properties     日志配置文件参数
            11.6-- logback.xml            日志主配置文件
        
  详见 doc/frame
`打包：
    gradle build -xtest`
    
`运行 :
    java -jar frame-0.0.1.jar`
    
` 日志文件目录：/storage/logs(假如应用没有权限创建，
        手动执行：sudo mkdir /storage;chmod 777 -R /storage)`  
