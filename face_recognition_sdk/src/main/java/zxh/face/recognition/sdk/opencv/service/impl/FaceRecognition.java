package zxh.face.recognition.sdk.opencv.service.impl;

import zxh.face.recognition.sdk.opencv.exception.WrongInputFaceImageException;
import zxh.face.recognition.sdk.opencv.service.IFaceRecognition;
import org.opencv.core.*;
import org.opencv.face.LBPHFaceRecognizer;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Stream;

/**
 * face recognition service
 * @author zxh
 * @date 2018-03-01
 */
public class FaceRecognition implements IFaceRecognition {

    /**
     * load openCV library
     */
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    /**
     * initialize userId-label map
     */
    private static ConcurrentMap<Integer, String> userId2Label = new ConcurrentHashMap<>();

    /**
     * the directory of face library
     */
    private String faceLibraryDirectory;

    /**
     * the path of face detection classifier
     */
    private String classifierPath;

    /**
     * the path of face recognizer
     */
    private String faceRecognizerPath;

    /**
     * constructor to initialize two properties
     * @param faceLibraryDirectory the directory of face library
     * @param classifierPath the path of face detection classifier
     */
    public FaceRecognition(String faceLibraryDirectory, String classifierPath) throws FileNotFoundException {

        // set value
        this.faceLibraryDirectory = faceLibraryDirectory;
        this.classifierPath = classifierPath;

        faceRecognizerPath = faceLibraryDirectory + "trainedFaceRecognizer/";

        // check value
        validatePaths();

        // load userId - label map
        loadUserId2LabelMap();
    }

    /**
     * detect face from image then add it to face library
     * @param srcImagePath  original image path
     * @param faceId face id
     */
    @Override
    public void addImageToFaceLibrary(String srcImagePath, String faceId) throws FileNotFoundException, WrongInputFaceImageException {

        //validate input path
        File img = new File(srcImagePath);
        if(!img.exists()) {
            throw new FileNotFoundException("Input image is not exist!");
        }

        //load original image
        Mat srcImage = readImage(srcImagePath);

        //detect faces
        Mat face = detectAndCutFace(srcImage);

        //save faces to face library
        saveFacesToLibrary(face, faceId);

        //update the face recognition train model
        train();

    }

    /**
     * recognize the given image then return the face id
     * @param srcImagePath image path
     * @return result map with two keys:
     *          1. userId: the face id which recognizer have found;
     *          2. confidence:  the confidence value of the corresponding id, the smaller the value, the higher the confidence
     */
    @Override
    public Map<String, Object> recognizeFace(String srcImagePath) throws FileNotFoundException, WrongInputFaceImageException {

        //validate input path
        File img = new File(srcImagePath);
        if(!img.exists()) {
            throw new FileNotFoundException("Input image is not exist!");
        }

        //load original image
        Mat srcImage = readImage(srcImagePath);

        //detect face
        Mat face = detectAndCutFace(srcImage);

        //recognize
        return predict(face);

    }

    /**
     * validate library classifier recognizer input image paths
     */
    private void validatePaths() throws FileNotFoundException {

        File faceLib = new File(faceLibraryDirectory);
        if(!faceLib.exists() || !faceLib.isDirectory()) {
            throw new FileNotFoundException("Face library directory is not exist or is not a directory!");
        }

        File classifier = new File(classifierPath);
        if(!classifier.exists()) {
            throw new FileNotFoundException("Classifier is not exist!");
        }

        File faceRecognizer = new File(faceRecognizerPath);
        if(!faceRecognizer.exists() || !faceRecognizer.isDirectory())  {

            if (!faceRecognizer.mkdir()){
                throw new FileNotFoundException(
                        "Attempt to create face recognizer directory error! The directory is : " + faceRecognizerPath);
            }
        }
    }

    /**
     * read input image
     * @param srcImagePath input image path
     * @return image of Mat
     */
    private Mat readImage(String srcImagePath) {

        Mat img = new Mat();
        Imgproc.equalizeHist(Imgcodecs.imread(srcImagePath, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE), img);
        return img;
    }

    /**
     * detect face then cut it by rectangle from original image,
     * if detect more than 1 face, then throw exception to declare the input image is error
     * @param srcImage input image of Mat
     * @return cut and resized face
     */
    private Mat detectAndCutFace(Mat srcImage) throws WrongInputFaceImageException {

        CascadeClassifier detector = new CascadeClassifier(classifierPath);
        MatOfRect faceDetections = new MatOfRect();

        detector.detectMultiScale(srcImage, faceDetections);

        Rect[] rectArray = faceDetections.toArray();
        if(!Integer.valueOf(1).equals(rectArray.length)) {
            throw new WrongInputFaceImageException(
                    "It's supposed to just 1 face detected, but there're "
                            + rectArray.length
                            + " faces found! Please re-input the original face image or provide a new one!");
        }

        Mat resizedFace = new Mat();
        Imgproc.resize(srcImage.submat(rectArray[0]), resizedFace, new Size(92, 112));

        return resizedFace;
    }

    /**
     * save face to face library
     * @param face face of Mat
     * @param faceId face id
     */
    private void saveFacesToLibrary(Mat face, String faceId) throws FileNotFoundException {

        // directory name format: face_(face_id)_(hasCode of face_id) OpenCV will use hashCode as label
        Integer faceHashCode = faceId.hashCode();
        if(!userId2Label.containsKey(faceHashCode)) {
            userId2Label.put(faceHashCode, faceId);
        }

        String faceBaseDirectory = faceLibraryDirectory + "/face_" + faceId + "_" + faceHashCode + "/";
        if(!new File(faceBaseDirectory).exists() && !new File(faceBaseDirectory).mkdir()) {

            throw new FileNotFoundException(
                        "Attempt to create face recognizer directory error! The directory is : " + faceRecognizerPath);
        }

        Imgcodecs.imwrite(faceBaseDirectory + System.currentTimeMillis() + ".jpeg", face);
    }

    private void loadUserId2LabelMap() {

        //read face library
        File faceLibDir = new File(faceLibraryDirectory);
        File[] faceLibUnits = faceLibDir.listFiles();
        if (faceLibUnits == null) {
            return;
        }

        Stream.of(faceLibUnits).forEach(
                faceDir -> {

                    if(!faceDir.isDirectory() || !faceDir.exists() || !faceDir.getName().startsWith("face_")){
                        return;
                    }

                    // directory name format: face_(face_id)_(hasCode of face_id) OpenCV will use hashCode as label
                    String[] splitFileNameArray = faceDir.getName().split("_");
                    // there should be 3 items in array
                    if(splitFileNameArray.length < 3) {
                        return;
                    }

                    userId2Label.put(Integer.valueOf(splitFileNameArray[2]), splitFileNameArray[1]);
                }
        );
    }

    /**
     * read the face library then generate the result map
     * @return result map with two keys:
     *              1. faces: face image of Mat list
     *              2. labels: face ids of Mat
     */
    private Map<String, Object> readFaceLibrary() throws FileNotFoundException {

        File faceLibDir = new File(faceLibraryDirectory);

        List<Mat> matOfFaces = new ArrayList<>();
        List<Integer> faceIds = new ArrayList<>();


        File[] faceLibUnits = faceLibDir.listFiles();
        if (faceLibUnits == null || faceLibUnits.length == 0) {
            throw new FileNotFoundException("Face library is empty!");
        }

        Stream.of(faceLibUnits).forEach(
                faceDir -> {

                    if(!faceDir.isDirectory() || !faceDir.exists() || !faceDir.getName().startsWith("face_")){
                        return;
                    }

                    File[] faces = faceDir.listFiles();
                    if (faces == null || faces.length == 0) {
                        return;
                    }

                    // directory name format: face_(face_id)_(hasCode of face_id) OpenCV will use hashCode as label
                    String[] splitFileNameArray = faceDir.getName().split("_");
                    // there should be 3 items in array
                    if(splitFileNameArray.length < 3) {
                        return;
                    }

                    Stream.of(faces).forEach( face -> {
                        matOfFaces.add(Imgcodecs.imread(face.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE));
                        faceIds.add(Integer.valueOf(splitFileNameArray[2]));
                    });
                }
        );

        Map<String, Object> mapOfFaceLibrary = new HashMap<>(2);
        mapOfFaceLibrary.put("faces", matOfFaces);
        MatOfInt label = new MatOfInt();
        label.fromList(faceIds);
        mapOfFaceLibrary.put("label", label);

        return mapOfFaceLibrary;
    }

    /**
     * train recognizer with face library
     */
    private void train() throws FileNotFoundException {

        Map<String, Object> mapOfFaceLibrary = readFaceLibrary();
        List<Mat> images = (List<Mat>)mapOfFaceLibrary.get("faces");
        Mat labels = (MatOfInt)mapOfFaceLibrary.get("label");

        LBPHFaceRecognizer lbphFaceRecognizer = LBPHFaceRecognizer.create();
        lbphFaceRecognizer.train(images, labels);
        lbphFaceRecognizer.write(faceRecognizerPath + "lbphFaceRecognizer.xml");

    }

    /**
     * predict face by recognizer
     * @param face face of Mat
     * @return result map with two keys:
     *          1. faceId: the face id which recognizer have found;
     *          2. confidence:  the confidence value of the corresponding id, the smaller the value, the higher the confidence
     */
    private Map<String, Object> predict(Mat face) {

        LBPHFaceRecognizer lbphFaceRecognizer = LBPHFaceRecognizer.create();
        lbphFaceRecognizer.read(faceRecognizerPath + "lbphFaceRecognizer.xml");

        int[] resultLabel = new int[]{-1};
        double[] resultConfidence = new double[]{0.0};

        lbphFaceRecognizer.predict(face, resultLabel, resultConfidence);

        Map<String, Object> resultMap = new HashMap<>(2);
        resultMap.put("faceId", userId2Label.get(resultLabel[0]));
        resultMap.put("confidence", resultConfidence[0]);

        return resultMap;

    }

}
