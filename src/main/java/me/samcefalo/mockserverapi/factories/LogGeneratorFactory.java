package me.samcefalo.mockserverapi.factories;

import lombok.Getter;
import me.samcefalo.mockserverapi.logs.DefaultLogGenerator;
import me.samcefalo.mockserverapi.logs.LogGenerator;
import me.samcefalo.mockserverapi.logs.PerRequestLogGenerator;

import java.util.Arrays;

/**
 * @author samcefalo
 * Factory to create a log generator
 */
@Getter
public enum LogGeneratorFactory {

    PER_REQUEST_PATH("PER_REQUEST_PATH", new PerRequestLogGenerator()),
    DEFAULT("DEFAULT", new DefaultLogGenerator());

    private String name;
    private LogGenerator logGenerator;

    LogGeneratorFactory(String name, LogGenerator logGenerator) {
        this.name = name;
        this.logGenerator = logGenerator;
    }

    public static LogGenerator getLogGenerator(String name) {
        return Arrays.stream(LogGeneratorFactory.values())
                .filter(logGeneratorFactory -> logGeneratorFactory.getName().equalsIgnoreCase(name))
                .findFirst().orElse(DEFAULT)
                .getLogGenerator();
    }

}
