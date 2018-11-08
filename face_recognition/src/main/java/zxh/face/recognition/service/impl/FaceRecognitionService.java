package zxh.face.recognition.service.impl;

import zxh.face.recognition.common.BaseComponent;
import zxh.face.recognition.common.util.BaseUtils;
import zxh.face.recognition.exception.internal.ServerInternalException;
import zxh.face.recognition.exception.internal.WrongImageException;
import zxh.face.recognition.sdk.opencv.exception.WrongInputFaceImageException;
import zxh.face.recognition.sdk.opencv.service.IFaceRecognition;
import zxh.face.recognition.sdk.opencv.service.impl.FaceRecognition;
import zxh.face.recognition.service.IFaceRecognitionService;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.util.Map;

/**
 * face recognition service implementation
 * @author zxh
 * @date 2018/03/05
 */
@Service
public class FaceRecognitionService extends BaseComponent implements IFaceRecognitionService {

    @Override
    public Map<String, Object> recognition(String inputImagePath) {

        //process the recognition, if the input image is wrong, throw a exception
        Map<String, Object> resultMap;

        IFaceRecognition faceRecognition;
        try {

            faceRecognition = new FaceRecognition(faceLibraryDir, classifierPath);
            resultMap = faceRecognition.recognizeFace(inputImagePath);

        } catch (FileNotFoundException e) {
            LOGGER.error("internal error!", e);
            throw new ServerInternalException("服务器内部错误：" + e.getMessage());
        } catch (WrongInputFaceImageException e) {
            throw new WrongImageException("无效的用户头像，请重新上传！");
        } finally {

            //no matter the recognize result, input image file should be deleted
            BaseUtils.INSTANCE.deleteTmpFile(inputImagePath);
        }

        //if every steps are doing good, then assemble the result map
        resultMap.put("userId", resultMap.get("faceId"));
        resultMap.remove("faceId");

        return resultMap;
    }
}
