package com.angel.fym.services;

import com.angel.fym.entity.Client;
import com.angel.fym.entity.Token;
import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.util.FileManager;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;

public class AuthorizeLogoutService extends AuthorizeService implements BaseExecutable {

    public AuthorizeLogoutService(Request request, Response response) {
        super(request, response);
    }

    public String action() {
        boolean correct = this.validateToken();

        if (!correct) {
            this.invalidResponse();
            return this.responseMessage;
        }

        JsonObject data = new JsonObject();
        data.addProperty("clean", true);

        this.correctResponse(data);
        return this.responseMessage;
    }

    private boolean validateToken() {
        int[] codes = Client.CLIENT_CODES;
        String token = this.getToken();
        boolean correct = false;

        for (int clientId : codes) {
            if (this.validateToken(clientId, token)) {
                this.cleanToken(clientId);
                correct = true;
                break;
            }
        }

        return correct;
    }

    private void cleanToken(int clientId) {
        Token token = new Token();
        token.setToken("");
        String content = (new Gson()).toJson(token);
        FileManager fileManager = new FileManager();
        fileManager.setContentToFile("token/" + clientId + ".json", content);
    }
}
