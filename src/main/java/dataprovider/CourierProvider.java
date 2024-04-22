package dataprovider;

import org.apache.commons.lang3.RandomStringUtils;
import request.CreateCourierRequest;

public class CourierProvider {
    public static CreateCourierRequest getRandomCreateCourierRequest() {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest();
        createCourierRequest.setLogin(RandomStringUtils.randomAlphanumeric(8));
        createCourierRequest.setPassword(RandomStringUtils.randomAlphabetic(8));
        createCourierRequest.setFirstName(RandomStringUtils.randomAlphabetic(8));

        return createCourierRequest;
    }

    public static CreateCourierRequest getRandomWithoutFirstNameCourierRequest() {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest();
        createCourierRequest.setLogin(RandomStringUtils.randomAlphanumeric(8));
        createCourierRequest.setPassword(RandomStringUtils.randomAlphabetic(8));
        createCourierRequest.setFirstName(RandomStringUtils.randomAlphabetic(0));

        return createCourierRequest;
    }

    public static CreateCourierRequest getRandomWithoutLoginCourierRequest() {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest();
        createCourierRequest.setLogin(RandomStringUtils.randomAlphanumeric(0));
        createCourierRequest.setPassword(RandomStringUtils.randomAlphabetic(8));
        createCourierRequest.setFirstName(RandomStringUtils.randomAlphabetic(1));

        return createCourierRequest;
    }

    public static CreateCourierRequest getRandomWithoutPasswordCourierRequest() {
        CreateCourierRequest createCourierRequest = new CreateCourierRequest();
        createCourierRequest.setLogin(RandomStringUtils.randomAlphanumeric(8));
        createCourierRequest.setPassword(RandomStringUtils.randomAlphabetic(0));
        createCourierRequest.setFirstName(RandomStringUtils.randomAlphabetic(1));

        return createCourierRequest;
    }
}
