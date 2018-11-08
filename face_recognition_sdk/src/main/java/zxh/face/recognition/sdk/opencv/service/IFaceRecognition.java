package zxh.face.recognition.sdk.opencv.service;

import zxh.face.recognition.sdk.opencv.exception.WrongInputFaceImageException;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * face recognition service
 * @author zxh
 * @date 2018-03-01
 */
public interface IFaceRecognition {

    /**
     * detect face from image then add it to face library
     * @param srcImagePath  original image path
     * @param faceId face id
     */
    void addImageToFaceLibrary(String srcImagePath, String faceId) throws FileNotFoundException, WrongInputFaceImageException;

    /**
     * recognize the given image then return the face id
     * @param srcImagePath image path
     * @return face id
     */
    Map<String, Object> recognizeFace(String srcImagePath) throws FileNotFoundException, WrongInputFaceImageException;
}
