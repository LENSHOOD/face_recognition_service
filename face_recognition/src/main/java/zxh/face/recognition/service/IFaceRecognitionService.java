package zxh.face.recognition.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * face recognition service
 * @author zxh
 * @date 2018/03/05
 */
public interface IFaceRecognitionService {

    /**
     * process face recognition
     * @param inputImagePath image to recognize
     * @return result map with two keys:
     *          1. userId: the user id which recognizer have found;
     *          2. confidence: the confidence percentage of the corresponding user
     */
    Map<String, Object> recognition(String inputImagePath);
}
