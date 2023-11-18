# hmdp_learning
HMDP learning project.

## 2023/11/17
### 完成登录状态校验
![校验登录状态流程](/readmedir/1-1.png "校验登录状态流程")
### User -> UserDTO
防止敏感信息泄露。同时防止session存储信息过多，对服务器内存造成压力。
### session -> Redis ?
多台Tomcat并不共享session存储空间，当请求切换到不同tomcat服务时导致数据丢失的问题。

## 2023/11/18
在配置拦截器的过程中，需注意拦截器顺序。拦截器顺序由拦截器添加顺序决定。指定order不一定起作用。（？）