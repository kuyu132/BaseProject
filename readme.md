### 目的
根据这个项目可以快速的开发出一个新的项目来，提供常用的开发组件，并且绕过一些常见的开发过程中的坑。
#### config.gradle
看过很多项目后，发现它们的版本管理、第三方库管理等很混乱，所以便有了这个文件
#### 将module分文件夹管理
新建一个module，比如：baselib，删除module内的文件，如果有.iml文件则保留，没有删除其它文件后会自动产生，然后将其他module移动到baselib文件夹中，在setting.gradle中删除baselib，并修改其他module的路径即可

## 基础模块
### utils模块
常见的工具类，常见的数值计算、文件加密、toast、调试、设备和定位相关的
### network
resultapi风格示例、文件上传和下载、基础响应
### widget常用的控件
比如面包屑控件、自带清除按钮的EditText、监听器、常见的对话框、recyclerview相关、常用fragment

## app模块
如果是单个app的话可以直接使用，如果多个app的话看模块间是否有业务关联，有的话将公共业务模块抽出来，app依赖之，没有则彼此分离，如果模块间耦合严重的话可以使用Arouter框架来分离。


#### HttpService
网络请求示例，用rxjava封装

#### LoadImageUtils
glide图片加载封装

## 第三方库
比较常见的就不做说明了，比如：rx，glide等
#### BaseRecyclerViewAdapterHelper
非常好用的recyclerview的adapter框架
#### stetho和stethoOkhttp
可以在chrome上查看网络请求和数据库操作
#### rxpermission
用这个可以实现权限动态请求，里面的实现代码较少，可以自己写一遍
