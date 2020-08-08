package com.isaiahvaris;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MainTest  {
    //Test various methods in our main and ConfigParser

    @Test
    void productionValuesTest() {//production config file values
        ConfigParser testConfig = new ConfigParser("config.txt");
        assertEquals("sq04_db", testConfig.get("dbname"));
        assertEquals("127.0.0.1", testConfig.get("host"));
        assertEquals("fintek", testConfig.get("application.name"));
        assertEquals("8080", testConfig.get("application.port"));
        assertEquals("/api/v1", testConfig.get("application.context-url"));
        assertEquals("production", testConfig.get("mode"));
        assertEquals("green", testConfig.get("theme"));
        assertEquals("fast", testConfig.get("pipeline"));
    }

    @Test
    void stagingValuesTest() {//staging config file values
        ConfigParser testConfig = new ConfigParser("config.txt.staging");
        assertEquals("sq04_db", testConfig.get("dbname"));
        assertEquals("127.0.0.1", testConfig.get("host"));
        assertEquals("fintek-staging", testConfig.get("application.name"));
        assertEquals("8081", testConfig.get("application.port"));
        assertEquals("/api/v1", testConfig.get("application.context-url"));
        assertEquals("staging", testConfig.get("mode"));
        assertEquals("blue", testConfig.get("theme"));
        assertEquals("fast-staging", testConfig.get("pipeline"));
    }

    @Test
    void developmentValuesTest() {//development config file values
        ConfigParser testConfig = new ConfigParser("config.txt.dev");
        assertEquals("sq04_db-development", testConfig.get("dbname"));
        assertEquals("127.0.0.1", testConfig.get("host"));
        assertEquals("fintek-development", testConfig.get("application.name"));
        assertEquals("8082", testConfig.get("application.port"));
        assertEquals("/api/v1", testConfig.get("application.context-url"));
        assertEquals("development", testConfig.get("mode"));
        assertEquals("yellow", testConfig.get("theme"));
        assertEquals("fast-development", testConfig.get("pipeline"));
    }
}