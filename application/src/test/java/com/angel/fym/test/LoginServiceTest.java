package com.angel.fym.test;

import com.angel.fym.entity.Client;
import com.angel.fym.util.FileManager;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoginServiceTest {

    @Test
    public void testClientStructure() {
        FileManager fileManager = new FileManager();
        Gson json = new Gson();
        Client client = json.fromJson(fileManager.getContentFromFile("client/1020.json"), Client.class);
        assertEquals(1020, client.getId());
        assertEquals("peter1223@gmail.com", client.getEmail());
    }
}
