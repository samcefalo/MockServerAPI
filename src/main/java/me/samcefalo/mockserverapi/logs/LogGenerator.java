package me.samcefalo.mockserverapi.logs;

import lombok.Setter;
import me.samcefalo.mockserverapi.MockServerApiApplication;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.UUID;
import java.util.logging.Logger;

/**
 * @author samcefalo
 * Object to handle File Log Generation
 */
public abstract class LogGenerator {

    @Value("${mock.log_path_name}")
    private String pathName;
    @Value("${mock.log_file_type}")
    private String fileType = ".yml";
    @Setter
    private String fileName = "/mockserver_" + UUID.randomUUID().toString().substring(0, 5);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss.SSS");

    public void handle(String fileName, HttpRequest httpRequest, HttpResponse httpResponse) {
        this.setFileName(fileName);
        this.handle(httpRequest, httpResponse);
    }

    protected void handle(HttpRequest httpRequest, HttpResponse httpResponse) {
        try {
            ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
            Resource resource = resolver.getResource(pathName + fileName + fileType);

            File file = resource.getFile();
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }

            //Write Log File
            writeLog(file, httpRequest, httpResponse);
        } catch (IOException e) {
            Logger.getLogger("HttpLogInterceptor").warning("Invalid log path name: " + pathName);
        }
    }

    private void writeLog(File file, HttpRequest httpRequest, HttpResponse httpResponse) throws IOException {
        long time = System.currentTimeMillis();
        try (FileWriter fileWriter = new FileWriter(file, true)) {
            fileWriter.write(String.format("[%s] <Request>:", DATE_FORMAT.format(time)));
            fileWriter.write(System.lineSeparator());
            fileWriter.write(httpRequest.toString());
            fileWriter.write(System.lineSeparator());
            fileWriter.write(String.format("[%s] <Response>:", DATE_FORMAT.format(time)));
            fileWriter.write(System.lineSeparator());
            fileWriter.write(httpResponse.toString());
            fileWriter.write(System.lineSeparator());
            fileWriter.write(System.lineSeparator());
        }
    }

}
