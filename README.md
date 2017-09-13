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


## 二、查询桥接口

#### 请求方式：GET/POST
#### URL：url/searchBridge
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
uid | String |xxxxx|用户名|
searchType|String|bName|以什么方式查询，查询类型|
searchParam|String|柳沟村大桥（根据searchType来决定）|实际的查询参数|
searchAll|String|1或0|下面详解|

searchAll类型|
-----|
0，按条件查询|
当searchAll=1，searchType和searchParam参数不起作用，自动返回所有信息|

searchType的类型|
----------|
bName 桥名|

**注意需要在请求头中放入，在登陆成功时获得的Token，在Cookie中获取**

#### 返回参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | int || 下面详解|
data |  | |下面详解|

code类型|说明|
------|---|
-2|表明权限拒绝|
-1|表明参数错误|
0|代表无数据|
>=1|代表数据的个数|

data类型|说明|
-|-|
当code=-2，String|权限拒绝信息|
当code=-1，String|返回参数错误信息|
当code=0， String|返回无数据信息|
当code>=1，json|返回具体的数据|


**json字段详解参见数据库PDF**

json示例
```json

{
    "code": 3,
    "data": [
        {
            "fromToNum": "",
            "bName": "二郎岔纵向桥",
            "memo": ""
        },
        {
            "fromToNum": "",
            "bName": "冲沟大桥",
            "memo": ""
        },
        {
            "fromToNum": "",
            "bName": "唐山沟大桥",
            "memo": ""
        }
    ]
}

```

图片示例

![image](http://note.youdao.com/yws/api/personal/file/3BBD8931A30B44EFBE29443FEFB816C3?method=download&shareKey=1ed4df50d65b456f995bbe1f26a0bd06)


## 三、梁板查询

#### 请求方式：GET/POST
#### URL：url/searchBeam
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
uid | String |xxxxx|用户名|
searchType|String|bName，bID|以什么方式查询，查询类型|
searchParam|String|柳沟村大桥（根据searchType来决定）|实际的查询参数|
searchAll|String|1或0|下面详解|

searchAll类型|
-----|
0，按条件查询|
当searchAll=1，searchType和searchParam参数不起作用，自动返回所有信息|

searchType的类型|
----------|
bName 桥名|
bID 梁编号|

**注意需要在请求头中放入，在登陆成功时获得的Token，在Cookie中获取**

#### 返回参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | int || 下面详解|
data |  | |下面详解|

code类型|说明|
------|---|
-2|表明权限拒绝|
-1|表明参数错误|
0|代表无数据|
>=1|代表数据的个数|

data类型|说明|
-|-|
当code=-2，String|权限拒绝信息|
当code=-1，String|返回参数错误信息|
当code=0， String|返回无数据信息|
当code>=1，json|返回具体的数据|


**json字段详解参见数据库PDF**

```json

{
    "code": 1,
    "data": [
        {
            "concrete": 0,
            "steel": 0,
            "rbr": 0,
            "bName": "二郎岔纵向桥",
            "lsl": 0,
            "proBottom": 0,
            "guardRail": 0,
            "scale": 0,
            "lsr": 0,
            "rsl": 0,
            "air": 0,
            "down": 0,
            "endSize": 0,
            "grouting": 0,
            "proDown": 0,
            "rsr": 0,
            "stretch": 0,
            "len": 0,
            "qrCode": "",
            "top": 0,
            "hoisting": 0,
            "height": 0,
            "llb": 0,
            "lld": 0,
            "bottom": 0,
            "rlb": 0,
            "rld": 0,
            "slope": 0,
            "intensity": 0,
            "proTop": 0,
            "lls": 0,
            "casts": 0,
            "size": "",
            "keep": 0,
            "lbl": 0,
            "rls": 0,
            "upWarp": 0,
            "bID": "1",
            "lbr": 0,
            "rbl": 0,
            "status": "未预制"
        }
    ]
}

```
