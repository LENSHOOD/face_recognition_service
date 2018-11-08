## 基于 OpenCV 库的人脸识别 SDK
### Environment
```
OpenCV 3.4.0 for JAVA
JDK 1.8
```

### Features

    - 基于 OpenCV 开发
    - 人脸检测
    - 人脸识别

### Configuration

```
Maven：
<dependency>
	<groupId>com.kyee.cv.face.recognition</groupId>
	<artifactId>opencv_sdk</artifactId>
	<version>1.0-SNAPSHOT</version>
</dependency>
```

### Example
```
// 初始化， faceLibraryDir 人脸库目录， classifierPath分类器路径
IFaceRecognition faceRecognition = new FaceRecognition(faceLibraryDir, classifierPath);

// 识别图像并注册检测到的人脸
faceRecognition.addImageToFaceLibrary(inputImagePath, label);

// 识别图像并将检测到的人脸与人脸库对比
Map<String, Object> resultMap = faceRecognition.recognizeFace(inputImagePath);
```

