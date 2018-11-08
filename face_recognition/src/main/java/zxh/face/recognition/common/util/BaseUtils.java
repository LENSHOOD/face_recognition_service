package zxh.face.recognition.common.util;

import zxh.face.recognition.exception.internal.FileUploadException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * utilities
 * @author zxh
 * @date 2018/03/07
 */
public enum BaseUtils {

    /**
     * singleton instance
     */
    INSTANCE;

    /**
     * save the MultipartFile to given location
     * @param file MultipartFile
     * @param destPath destination location
     */
    public void saveUploadFile(MultipartFile file, String destPath) {

        if(file.isEmpty()) {
            throw new FileUploadException("文件上传错误：uploaded file is empty!");
        }

        try {
            file.transferTo((new File(destPath)));
        } catch (IOException e) {
            throw new FileUploadException("文件上传错误：save uploaded file failed! " + e.toString());
        }
    }

    /**
     * save the file with Base64 string to given location
     * @param base64String file with Base64 string
     * @param destPath destination location
     */
    public void saveUploadBase64StringFile(String base64String, String destPath) {

        //some browser may auto convert the '+' to '(space)'
        if(base64String.contains(" ")) {
            base64String = base64String.replaceAll(" ", "+");
        }

        byte[] base64Bytes = Base64.decodeBase64(base64String);
        try {
            FileUtils.writeByteArrayToFile(new File(destPath), base64Bytes);
        } catch (IOException e) {
            throw new FileUploadException("文件上传错误：save uploaded file failed! " + e.toString());
        }
    }

    /**
     * delete file
     * @param srcPath file path
     */
    public void deleteTmpFile(String srcPath) {

        try {
            Files.delete(Paths.get(srcPath));
        } catch (IOException e) {
            LoggerFactory.getLogger(this.getClass()).error("delete file error!", e);
        }

    }
}

