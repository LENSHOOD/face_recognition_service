package zxh.face.recognition.sdk.opencv;

import zxh.face.recognition.sdk.opencv.exception.WrongInputFaceImageException;
import zxh.face.recognition.sdk.opencv.service.IFaceRecognition;
import zxh.face.recognition.sdk.opencv.service.impl.FaceRecognition;
import org.junit.Test;

import java.io.FileNotFoundException;

public class FaceRecognitionTester {

    private static final String FACE_LIB_DIR = "/Users/alexanderzhang/Documents/work/qiyi/project/face_recognition_sdk/test_db";
    //private static final String CLASSIFIER_PATH = "/usr/local/Cellar/opencv/3.4.0_1/share/OpenCV/lbpcascades/lbpcascade_frontalface_improved.xml";
    private static final String CLASSIFIER_PATH = "/Users/alexanderzhang/Documents/work/qiyi/project/face_recognition_sdk/image_database/data/cascade.xml";


    @Test
    public void addImageToFaceLibraryTester() throws FileNotFoundException, WrongInputFaceImageException {

        String imagePath = "/Users/alexanderzhang/Documents/work/qiyi/project/face_recognition_sdk/test_db/input_image/profile.jpg";

        IFaceRecognition faceRecognition = new FaceRecognition(FACE_LIB_DIR, CLASSIFIER_PATH);

        faceRecognition.addImageToFaceLibrary(imagePath, "fatCat");

    }

    //@Test
    public void recognizeFaceTester() throws FileNotFoundException, WrongInputFaceImageException {

        String imagePath = "/Users/alexanderzhang/Documents/work/qiyi/project/opencv_demo/images/test_3.jpeg";

        IFaceRecognition faceRecognition = new FaceRecognition(FACE_LIB_DIR, CLASSIFIER_PATH);

        faceRecognition.recognizeFace(imagePath);
    }
}
