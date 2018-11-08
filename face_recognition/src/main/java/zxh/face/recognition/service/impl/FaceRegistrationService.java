package zxh.face.recognition.service.impl;

import zxh.face.recognition.common.BaseComponent;
import zxh.face.recognition.exception.internal.ServerInternalException;
import zxh.face.recognition.exception.internal.WrongImageException;
import zxh.face.recognition.sdk.opencv.exception.WrongInputFaceImageException;
import zxh.face.recognition.sdk.opencv.service.IFaceRecognition;
import zxh.face.recognition.sdk.opencv.service.impl.FaceRecognition;
import zxh.face.recognition.service.IFaceRegistrationService;
import zxh.face.recognition.common.util.BaseUtils;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;

/**
 * face registration service implementation
 * @author zxh
 * @date 2018/03/05
 */
@Service
public class FaceRegistrationService extends BaseComponent implements IFaceRegistrationService {

    @Override
    public void registration(String inputImagePath, String userId) {

        //process the recognition, if the input image is wrong, throw a exception
        IFaceRecognition faceRecognition;
        try {
            faceRecognition = new FaceRecognition(faceLibraryDir, classifierPath);
            faceRecognition.addImageToFaceLibrary(inputImagePath, userId);

        } catch (FileNotFoundException e) {
            LOGGER.error("file upload error!", e);
            throw new ServerInternalException("服务器内部错误：" + e.toString());
        } catch (WrongInputFaceImageException e) {
            throw new WrongImageException("无效的用户头像，请重新上传!");
        } finally {

            //no matter the recognize result, input image file should be deleted
            BaseUtils.INSTANCE.deleteTmpFile(inputImagePath);
        }

    }

}
