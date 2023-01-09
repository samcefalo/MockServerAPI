package me.samcefalo.mockserverapi.services;

import jakarta.annotation.PostConstruct;
import me.samcefalo.mockserverapi.logs.LogGenerator;
import me.samcefalo.mockserverapi.loaders.FileLoader;
import me.samcefalo.mockserverapi.models.ExpectationConfig;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.logging.Logger;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * @author samcefalo
 * Service to handle mockserver requests/responses
 */
@Service
public class MockService {

    @Autowired
    private ClientAndServer server;
    @Autowired
    private FileLoader fileLoader;
    @Value("${mock.save_log_enabled}")
    private boolean saveLogEnabled;
    @Autowired
    private LogGenerator logGenerator;

    @PostConstruct
    public void configure() {
        Logger.getLogger("MockService").info("Save logs to file enabled: " + saveLogEnabled);
        fileLoader.getExpectationConfigs().forEach(expectationConfig -> {

            HttpRequest httpRequest = request()
                    .withMethod(expectationConfig.getMockHttpRequest().getMethod())
                    .withPath(expectationConfig.getMockHttpRequest().getPath());

            int t = expectationConfig.getMockHttpRequest().getTimes();
            Times times = Times.unlimited();

            if (t > 0)
                times = Times.exactly(t);

            server.when(httpRequest, times)
                    .callback(request -> configureCallBackResponse(request, expectationConfig));
        });
    }

    //Call LogGenerator and return HttpResponse
    private HttpResponse configureCallBackResponse(HttpRequest httpRequest, ExpectationConfig expectationConfig) {
        String uuid = UUID.randomUUID().toString().substring(0, 5);

        HttpResponse httpResponse = response()
                .withBody(expectationConfig.getMockHttpResponse().getBody())
                .withStatusCode(expectationConfig.getMockHttpResponse().getCode())
                .withHeader("MockServer-UUID", uuid);

        if (saveLogEnabled) {
            logGenerator.handle(expectationConfig.getMockHttpRequest().getPath(), httpRequest, httpResponse);
        }

        return httpResponse;
    }

    public void stop() {
        server.stop();
    }

}
