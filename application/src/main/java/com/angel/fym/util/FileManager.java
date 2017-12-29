package com.angel.fym.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

public class FileManager {
    private static final String DOCKER_PATH = "/usr/ecommerce/src/main/resources/";

    private ClassLoader classLoader = getClass().getClassLoader();

    public String getContentFromFile(String fileName) {
        String result = "";

        try {
            return IOUtils.toString(this.getInputStream(fileName), "UTF-8");
        } catch (Exception e) {
            return result;
        }
    }

    public void setContentToFile(String fileName, String content) {
        try {

            File file = new File(this.getPath(fileName));
            FileUtils.writeStringToFile(file, content, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InputStream getInputStream(String filename) throws FileNotFoundException {
        if (OSValidator.isUnix()) {
            return new FileInputStream(DOCKER_PATH.concat(filename));
        }

        return this.classLoader.getResourceAsStream(filename);
    }

    private String getPath(String fileName) {
        if (OSValidator.isUnix()) {
            return DOCKER_PATH.concat(fileName);
        }

        URL resource = this.classLoader.getResource(fileName);
        return resource.getPath();
    }
}
