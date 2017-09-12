# ChinaRailWay后台接口文档

## 一级URL
url：http://139.199.20.248:8080/ChinaRailWay


## 一、登陆接口
#### 请求方式：GET/POST
#### URL：url/login
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
uid | String |xxxxx|用户名|
pwd | String|abc123456|密码|


#### 返回参数
返回类型：json

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | int |1或0|成功返回1，失败返回0
data | json | 示例在下面|返回成功时，会有此参数，具体的信息|

**data参数类型，当code=0，data类型为String，表示错误信息，当code=1，data类型为Users**

**Users类型说明**

字段名 | 类型 | 说明 |
-------|------|------|
uid | String |用户名|
Uname | String|姓名|
roleId|int| |
state|int| |

```json
{
    "code": 1,
    "data": {
        "uid": "admin",
        "uname": "张经理",
        "roleId": 1,
        "state": 0
    }
}
```
