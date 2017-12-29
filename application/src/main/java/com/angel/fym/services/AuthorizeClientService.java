package com.angel.fym.services;

import com.angel.fym.entity.Client;
import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.util.FileManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spark.Request;
import spark.Response;

public class AuthorizeClientService extends AuthorizeService implements BaseExecutable {
    private FileManager fileManager;

    public AuthorizeClientService(Request request, Response response) {
        super(request, response);
        this.fileManager = new FileManager();
    }

    @Override
    public String action() {
        JsonObject client = this.getClientByToken();

        if (client == null) {
            this.invalidResponse();
            return this.responseMessage;
        }

        this.correctResponse(client);

        return this.responseMessage;
    }

    protected JsonObject getClientByToken() {
        int[] codes = Client.CLIENT_CODES;
        String token = this.getToken();
        JsonObject client = null;

        for (int clientId : codes) {
            if (this.validateToken(clientId, token)) {
                JsonParser jsonParser = new JsonParser();
                StringBuilder builder = new StringBuilder();
                builder.append("client/").append(clientId).append(".json");
                client = jsonParser.parse(fileManager.getContentFromFile(builder.toString())).getAsJsonObject();
                client.remove("password");
                break;
            }
        }

        return client;
    }
}
