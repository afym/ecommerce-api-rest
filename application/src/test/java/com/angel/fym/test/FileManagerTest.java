package com.angel.fym.test;

import com.angel.fym.util.FileManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileManagerTest {

    @Test
    public void testContentFromFile() {
        FileManager fileManager = new FileManager();
        String var = fileManager.getContentFromFile("test/foo");
        assertEquals("var", var);
    }

    @Test
    public void testContentToFileWriteOne() {
        FileManager fileManager = new FileManager();
        fileManager.setContentToFile("test/foo", "fu");
        String file = fileManager.getContentFromFile("test/foo");
        assertEquals("fu", file);
    }

    @Test
    public void testContentToFileWriteTwo() {
        FileManager fileManager = new FileManager();
        fileManager.setContentToFile("test/foo", "var");
        String file = fileManager.getContentFromFile("test/foo");
        assertEquals("var", file);
    }
}
