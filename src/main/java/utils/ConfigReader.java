package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {

    private static final Properties PROPERTIES = new Properties();

    static {
        load("src/test/resources/config.properties");
    }

    private ConfigReader() {
    }

    private static void load(String path) {
        try (InputStream inputStream = new FileInputStream(path)) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Unable to load configuration file: " + path, e);
        }
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        if (systemValue != null && !systemValue.isBlank()) {
            return systemValue;
        }
        return PROPERTIES.getProperty(key);
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = get(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public static int getInt(String key, int defaultValue) {
        String value = get(key);
        return value == null || value.isBlank() ? defaultValue : Integer.parseInt(value);
    }
}
