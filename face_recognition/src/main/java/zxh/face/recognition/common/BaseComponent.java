package zxh.face.recognition.common;

import zxh.face.recognition.vo.BaseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * base component for controller and service
 * provide basic parameter check and common fields
 * @author zxh
 * @date 2018/03/07
 */
public abstract class BaseComponent {

    /**
     * input image temporary directory, which to temporary store the upload image,
     * configure it at "application.properties" use property: face.recognition.inputImageTmpDir
     */
    @Value("${face.recognition.inputImageTmpDir}")
    protected String inputImageTmpDir;

    /**
     * face database directory, which to store the face images, every user has it's own folder named "face_{userID}"
     * the registration face will be saved at "face_{userId}/{random number}.jpeg",
     * configure it at "application.properties" use property: face.recognition.faceLibraryDir
     */
    @Value("${face.recognition.faceLibraryDir}")
    protected String faceLibraryDir;

    /**
     * opencv face detection classifier path, you can find it at opencv base directory,
     * configure it at "application.properties" use property: face.recognition.classifierPath
     */
    @Value("${face.recognition.classifierPath}")
    protected String classifierPath;

    /**
     * logger
     */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BaseResponse.class);

    /**
     * check the user appoint location/file is valid
     */
    @PostConstruct
    private void startupCheck() {

        File inputImageTmpDirFile = new File(inputImageTmpDir);
        if(!inputImageTmpDirFile.exists() || !inputImageTmpDirFile.isDirectory()) {
            throw new RuntimeException("input image tmp directory doesn't exist !");
        }

        File faceLibraryDirFile = new File(faceLibraryDir);
        if(!faceLibraryDirFile.exists() || !faceLibraryDirFile.isDirectory()) {
            throw  new RuntimeException("face library directory doesn't exist !");
        }

        File classifierPathFile = new File(classifierPath);
        if(!classifierPathFile.exists()) {
            throw  new RuntimeException("classifier file doesn't exist !");
        }
    }

}
