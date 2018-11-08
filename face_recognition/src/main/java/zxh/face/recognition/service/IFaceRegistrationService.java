package zxh.face.recognition.service;

/**
 * face registration service
 * @author zxh
 * @date 2018/03/05
 */
public interface IFaceRegistrationService {

    /**
     * process face registration
     * @param inputImagePath image to recognize
     * @param userId user id
     */
    void registration(String inputImagePath, String userId);
}
