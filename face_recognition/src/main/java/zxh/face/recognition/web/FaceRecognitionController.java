package zxh.face.recognition.web;

import zxh.face.recognition.common.BaseComponent;
import zxh.face.recognition.common.util.BaseUtils;
import zxh.face.recognition.service.IFaceRecognitionService;
import zxh.face.recognition.service.IFaceRegistrationService;
import zxh.face.recognition.vo.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * face recognition service controller
 * @author zxh
 * @date 2018/03/05
 */
@RestController
@RequestMapping("/v1/face")
public class FaceRecognitionController extends BaseComponent {

    private final IFaceRegistrationService faceRegistrationService;
    private final IFaceRecognitionService faceRecognitionService;

    /**
     * inject two services bean
     * @param faceRegistrationService face registration service
     * @param faceRecognitionService face recognition service
     */
    @Autowired
    public FaceRecognitionController(
            IFaceRegistrationService faceRegistrationService,
            IFaceRecognitionService faceRecognitionService) {

        this.faceRegistrationService = faceRegistrationService;
        this.faceRecognitionService = faceRecognitionService;
    }

    /**
     * registration interface
     * @param userImage image to register
     * @param userId user id
     * @return return the successful BaseResponse to caller
     */
    @PostMapping("/registration")
    public BaseResponse registration(@RequestParam("userImage") MultipartFile userImage, @RequestParam("userId") String userId) {

        //save the input image to user appoint location
        String inputImagePath = inputImageTmpDir + System.currentTimeMillis();
        BaseUtils.INSTANCE.saveUploadFile(userImage, inputImagePath);

        faceRegistrationService.registration(inputImagePath, userId);

        BaseResponse successful = new BaseResponse();
        successful.setSuccess(true);
        successful.setTimestamp(System.currentTimeMillis());
        successful.setErrorCode("0000");
        successful.setData("注册成功！");

        return successful;
    }

    /**
     * registration interface with Base64 string image type
     * @param userImage image to register
     * @param userId user id
     * @return return the successful BaseResponse to caller
     */
    @PostMapping("/base64/registration")
    public BaseResponse registrationWithBase64(@RequestParam("userImage") String userImage, @RequestParam("userId") String userId) {

        //save the input image to user appoint location
        String inputImagePath = inputImageTmpDir + System.currentTimeMillis();
        BaseUtils.INSTANCE.saveUploadBase64StringFile(userImage, inputImagePath);

        faceRegistrationService.registration(inputImagePath, userId);

        BaseResponse successful = new BaseResponse();
        successful.setSuccess(true);
        successful.setTimestamp(System.currentTimeMillis());
        successful.setErrorCode("0000");
        successful.setData("注册成功！");

        return successful;
    }

    /**
     * recognition interface
     * @param userImage image to recognize
     * @return return the successful BaseResponse to caller
     */
    @PostMapping("/recognition")
    public BaseResponse recognition(@RequestParam("userImage") MultipartFile userImage) {

        //save the input image to user appoint location
        String inputImagePath = inputImageTmpDir + System.currentTimeMillis();
        BaseUtils.INSTANCE.saveUploadFile(userImage, inputImagePath);

        //process recognition then return
        BaseResponse successful = new BaseResponse();
        successful.setData(faceRecognitionService.recognition(inputImagePath));
        successful.setSuccess(true);
        successful.setTimestamp(System.currentTimeMillis());
        successful.setErrorCode("0000");

        return successful;
    }

    /**
     * recognition interface  with Base64 string image type
     * @param userImage image to recognize
     * @return return the successful BaseResponse to caller
     */
    @PostMapping("/base64/recognition")
    public BaseResponse recognitionWithBase64(@RequestParam("userImage") String userImage) {

        //save the input image to user appoint location
        String inputImagePath = inputImageTmpDir + System.currentTimeMillis();
        BaseUtils.INSTANCE.saveUploadBase64StringFile(userImage, inputImagePath);

        //process recognition then return
        BaseResponse successful = new BaseResponse();
        successful.setData(faceRecognitionService.recognition(inputImagePath));
        successful.setSuccess(true);
        successful.setTimestamp(System.currentTimeMillis());
        successful.setErrorCode("0000");

        return successful;
    }
}
