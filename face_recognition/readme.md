### 人脸识别服务
- 配置：
> application.properties

```
face.recognition.inputImageTmpDir = 上传图片临时目录
face.recognition.faceLibraryDir = 人脸数据库目录
face.recognition.classifierPath = OpenCV分类器路径
```
- 启动：
```
mvn package && java -jar target/face_recognition-0.0.1-SNAPSHOT.jar 
```
- 接口说明
    ```
    接口：注册用户头像 ({base64}为可选字段，增加后，接口改为接收 base64 String 的图片形式)
    POST http://ip:port/v1/face/{base64}/registration
    
    请求参数：
    1. userImage : MultipartFile (base64模式时为 String) 采集到的用户头像
    2. userId : String  用户 ID
    
    响应：
    {
        "success" : boolean,  //成功返回 true，失败返回 false
        "timestamp" : Long(Number),  //当前时间
        "errorCode" : String //错误代码，详见下文
        "data" : String  //成功返回注册成功，失败返回失败原因
    }
    
    
    接口：识别用户头像 ({base64}为可选字段，增加后，接口改为接收 base64 String 的图片形式)
    POST http://ip:port/v1/face/{base64}/recognition
    
    请求参数：
    1. userImage : MultipartFile (base64模式时为 String) 采集到的用户头像
    
    响应：
    {
        "success" : boolean,  //成功返回 true，失败返回 false
        "timestamp" : Long(Number),  //当前时间
        "errorCode" : String //错误代码，详见下文
        //success = true，data 值返回 如下的 JSON String
        //success = false，data 值返回 String 类型错误原因
        "data" : {  
            "userId" : String, //用户 ID
            "confidence" : String, //对比置信度
            "comment" : Stirng //备注
        }
    }
    ```
- 错误代码说明
    
errorCode | 说明
---|---
0000 | 正常
0001 | 上传文件错误
0002 | 服务器内部错误
0003 | 检测失败，需要重新采集（未检测到人脸）
