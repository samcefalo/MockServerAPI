package me.samcefalo.mockserverapi.logs;

import me.samcefalo.mockserverapi.MockServerApiApplication;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

/**
 * @author samcefalo
 * Per Request Path log generator: Create a log file per request path
 */
public class PerRequestLogGenerator extends LogGenerator {

    public PerRequestLogGenerator() {
    }

    @Override
    public void handle(String fileName, HttpRequest httpRequest, HttpResponse httpResponse) {
        fileName = fileName.replace("/", "_");
        this.setFileName("/mockserver" + fileName + "_" + MockServerApiApplication.id());
        super.handle(httpRequest, httpResponse);
    }

}
