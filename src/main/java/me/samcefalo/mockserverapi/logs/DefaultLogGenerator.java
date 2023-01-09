package me.samcefalo.mockserverapi.logs;

import me.samcefalo.mockserverapi.MockServerApiApplication;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;

/**
 * @author samcefalo
 * Default log generator: Create a log file per application initialization
 */
public class DefaultLogGenerator extends LogGenerator {

    public DefaultLogGenerator() {
        this.setFileName("/mockserver_" + MockServerApiApplication.startTimeStamp());
    }

    public void handle(String fileName, HttpRequest httpRequest, HttpResponse httpResponse) {
        super.handle(httpRequest, httpResponse);
    }

}
