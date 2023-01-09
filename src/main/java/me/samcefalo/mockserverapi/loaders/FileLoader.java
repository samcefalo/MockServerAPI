package me.samcefalo.mockserverapi.loaders;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import me.samcefalo.mockserverapi.models.MockHttpRequest;
import me.samcefalo.mockserverapi.models.MockHttpResponse;
import me.samcefalo.mockserverapi.models.ExpectationConfig;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

/**
 * @author samcefalo
 * ExpectationConfig files loader from ${mock.config_path_name}
 */
@Getter
@Component
public class FileLoader {

    @Value("${mock.config_path_name}")
    private String pathName;
    private final Set<ExpectationConfig> expectationConfigs = new HashSet<>();

    @PostConstruct
    private void load() {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

            //check if config path exists or create default
            this.checkConfigExistsOrCreateDefault(resolver);

            Resource[] resources = resolver.getResources(this.pathName + "/*.json");

            Logger.getLogger("FileLoader").info(String.format("Loading all expectation.json files from %s...", this.pathName));
            ObjectMapper mapper = new ObjectMapper();

            for (Resource resource : resources) {
                Logger.getLogger("FileLoader").info(String.format("Loading file %s...", resource.getFilename()));

                JsonNode root = mapper.readTree(resource.getInputStream());
                ExpectationConfig expectationConfig = getJsonConfig(root);

                expectationConfigs.add(expectationConfig);
                Logger.getLogger("FileLoader").info(String.format("Loaded file %s!", resource.getFilename()));
            }

            Logger.getLogger("FileLoader").info("Loaded all expectation files!");
        } catch (NullPointerException e) {
            Logger.getLogger("FileLoader").warning("NullPointerException: " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void checkConfigExistsOrCreateDefault(ResourcePatternResolver resolver) throws IOException {
        Resource r = resolver.getResource(this.pathName + "/");
        File file = r.getFile();
        if (!file.exists()) {
            Logger.getLogger("FileLoader").info(String.format("Creating empty path %s...", file.getPath()));
            //create default config and copy classpath:default_configs
            file.mkdirs();
            this.cloneDefaultConfig(resolver, r);
        }
    }

    private void cloneDefaultConfig(ResourcePatternResolver resolver, Resource outputResource) throws IOException {
        Resource[] resources = resolver.getResources("classpath*:default_configs/*.json");

        for(Resource r : resources) {
            // copy file to another path
            InputStream inputStream = r.getInputStream();
            try (OutputStream outputStream = new FileOutputStream(outputResource.getFile().getPath() + "/" + r.getFilename())) {
                IOUtils.copy(inputStream, outputStream);
            }
        }
    }

    private ExpectationConfig getJsonConfig(JsonNode root) {
        //request
        String httpRequestMethod = root.get("httpRequest").get("method").asText();
        String httpRequestPath = root.get("httpRequest").get("path").asText();
        Integer httpRequestTimes = root.get("httpRequest").get("times").asInt();
        //response
        String httpResponseBody = root.get("httpResponse").get("body").toString();
        Integer httpResponseCode = root.get("httpResponse").get("statusCode").asInt();

        MockHttpRequest mockHttpRequest = new MockHttpRequest(httpRequestMethod, httpRequestPath, httpRequestTimes);
        MockHttpResponse mockHttpResponse = new MockHttpResponse(httpResponseCode, httpResponseBody);

        return new ExpectationConfig(mockHttpRequest, mockHttpResponse);
    }

}
