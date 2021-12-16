package net.hydrogen2oxygen.se;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class EnvironmentTest {

    @Test
    public void testJsonParsing() throws IOException {

        Environment exampleEnvironment = new Environment();
        exampleEnvironment.setName("simple");
        exampleEnvironment.getData().put("userName","John");
        exampleEnvironment.getData().put("baseUrl","https://blabla.thisandthat.com");
        exampleEnvironment.getData().put(Se.SCREENSHOTS_PATH,"target/protocols/");
        exampleEnvironment.getData().put(Se.PROTOCOLS_PATH,"target/protocols/");
        exampleEnvironment.getData().put(Se.HEADLESS,"true");
        exampleEnvironment.getData().put("nThreads","8");
        exampleEnvironment.getData().put("parallel.timeout.minutes","15");
        exampleEnvironment.getData().put("ping.timeout.milliseconds","5000");

        ObjectMapper objectMapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);;
        objectMapper.configure(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY, true);
        File resultFile = new File("exampleEnvironment.json");
        objectMapper.writeValue(resultFile, exampleEnvironment);

        exampleEnvironment = objectMapper.readValue(resultFile, Environment.class);
        Assertions.assertEquals("simple", exampleEnvironment.getName());
        Assertions.assertEquals("John", exampleEnvironment.getData().get("userName"));
        Assertions.assertEquals("https://blabla.thisandthat.com", exampleEnvironment.getData().get("baseUrl"));

        // inheritance of another environment
        Environment baseEnvironment = new Environment();
        baseEnvironment.setName("base");
        baseEnvironment.getData().put("common.value","abc");

        exampleEnvironment.addEnvironment(baseEnvironment);
        Assertions.assertEquals("abc", exampleEnvironment.getData().get("common.value"));

    }
}
