# ChinaRailWay后台接口文档

## 一级URL
url：http://139.199.20.248:8080/ChinaRailWay

**注意：接口中所有包含中文的参数，提交前请使用URLEncoder进行编码，编码格式UTF-8**


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
大于等于1|代表数据的个数|

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
大于等于1|代表数据的个数|

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

## 四、组合查询

#### 请求方式：POST/GET
#### URL：url/search
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
uid | String |xxxxx|用户名|
type| String||下面详解|
searchType|String|bName，bID|以什么方式查询，查询类型|
searchParam|String|柳沟村大桥（根据searchType来决定）|实际的查询参数|
searchAll|String|1或0|下面详解|


type类型|说明|
--------|----|
beam|梁板查询|
bridge|桥查询|
buildPlan|架梁计划查询|
task|生产计划查询|
checkRec|检测记录|
factory|梁场查询|
files|文件查询|
makePosition|制梁台座|
model|模型|
monthData|月结|
store|库存|
storePosition|存梁台座|


searchAll类型|
-----|
0，按条件查询|
当searchAll=1，searchType和searchParam参数不起作用，自动返回所有信息|

searchType的类型|说明|
----------|--------|
bName |桥名|
bID |梁编号|
name|梁场名称|
BuildID|计划编号|
date|日期
MakePosID|制梁台号|
pos|位置|
PedID|台号|
ID| 模型ID|
sDate|年月|

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
大于等于1|代表数据的个数|

data类型|说明|
-|-|
当code=-2，String|权限拒绝信息|
当code=-1，String|返回参数错误信息|
当code=0， String|返回无数据信息|
当code>=1，json|返回具体的数据|


## 五、上传文件

#### 请求方式：POST
#### URL：url/fileUpload
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
bID | String |1|梁板编号|
bName| String|二郎岔纵向桥|桥名|

#### 请求头参数
参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
FileType | String |image或video|文件类型|
FileName| String|2017-5-5-大桥-1梁板|文件名|
FileSuffix|String|jpg|文件名后缀|

#### 返回参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | int || 下面详解|
data |  String| |返回错误信息详情|

code类型|说明|
------|---|
-1|上传失败|
大于等于0|代表上传成功，代表文件大小|


## 六、增加数据

#### 请求方式：POST
#### URL：url/addInfo
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
type | String |beam|增加的表名|
data| json||该表的数据json|


##### 参数 type类型说明

type类型|说明|
--------|----|
beam|梁板|
bridge|桥|
buildPlan|架梁计划|
task|生产计划|
checkRec|检测记录|
factory|梁场|
files|文件|
makePosition|制梁台座|
model|模型|
monthData|月结|
store|库存|
storePosition|存梁台座|


#### 返回参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | int || 1成功，-1失败|
data |  String| 参数错误等|返回错误信息详情|


## 七、修改数据

#### 请求方式：POST
#### URL：url/modify
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
type | String ||表的类型|
pk| json||被修改数据的主键和外键|
modifyData|json|下面详解|修改的数据json|


##### 参数 type类型说明

type类型|说明|
--------|----|
beam|梁板|
bridge|桥|
buildPlan|架梁计划|
task|生产计划|
checkRec|检测记录|
factory|梁场|
files|文件|
makePosition|制梁台座|
model|模型|
monthData|月结|
store|库存|
storePosition|存梁台座|


##### 参数 pk类型说明

一个数组
其中以name为主键外键名称，data为主键外键的值

```json

[
    {
        "data": "2",
        "name": "id"
    },
    {
        "data": "1",
        "name": "bid"
    },
    {
        "data": "二郎岔纵向桥",
        "name": "bName"
    }
]

```

##### 参数modifyData类型说明

name为，被修改数据的字段名，data为该字段名的数据

```json

[
    {
        "data": "lzl",
        "name": "name"
    }
]

```

#### 返回参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | int || 大于等于1成功，-1失败|
data |  String| 参数错误等|返回错误信息详情|

code为-1表示修改失败，请检查参数，code>=1表示成功修改的条数


## 八、文件查询

#### 请求方式：POST
#### URL：url/fileSearch
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
bID| String ||梁编号|
bName| String|| 桥名 |


#### 返回参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | int || 大于等于1成功，-1失败|
data |  String| |下面详解|

code为-1表示修改失败，请检查参数，code = 0 代表没有数据，code>=1表示查询到的文件总数

code = -1 时 data为错误信息，code>=1时 data为json格式数据

data是一个Array

|参数名|解释|
|-|-|
|file|文件路径|
|bName|桥名|
|bID|梁板编号|
|id|文件编号|
|name|文件名，下载文件需要提交文件名|
|type|文件类型，image图片，video视频|
返回数据示例
```json
{
    "code": 5,
    "data": [
        {
            "file": "C:acceptimages-2005249296.png",
            "bName": "二郎岔纵向桥",
            "name": "-2005249296.png",
            "id": -2005249296,
            "bID": "1",
            "type": "image"
        },
        {
            "file": "D:acceptimages-1830519012.png",
            "bName": "二郎岔纵向桥",
            "name": "-1830519012.png",
            "id": -1830519012,
            "bID": "1",
            "type": "image"
        },
        {
            "bName": "二郎岔纵向桥",
            "name": "lzl",
            "id": 2,
            "bID": "1"
        },
        {
            "file": "accpet1.jgp",
            "bName": "二郎岔纵向桥",
            "name": "nameiswjb",
            "id": 123,
            "bID": "1",
            "type": "jpg"
        },
        {
            "file": "accpet1.jgp",
            "bName": "二郎岔纵向桥",
            "name": "nameiswjb",
            "id": 1234,
            "bID": "1"
        }
    ]
}
```

## 九、文件下载

#### 请求方式：POST
#### URL：url/download
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
type| String |image或video|文件类型 image图片，video视频|
fileName| String|xxx.jpg| 文件名 |

fileName 文件名，需要通过使用文件查询接口，查询结果中的文件名才可以获取文件

#### 返回数据
返回文件字节，请读入并写入文件即可

## 十、梁板出入场

#### 请求方式：POST
#### URL：url/changeBeamStatus
#### 请求参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
type| String |已入库、已出库||
beamId| String|| 梁板编号 |
bName | String ||桥名|


#### 返回参数

参数名 | 类型 | 示例 | 说明|
-------|------|------|-----|
code | int || 1成功，其他失败|
data |  String| |错误信息|
