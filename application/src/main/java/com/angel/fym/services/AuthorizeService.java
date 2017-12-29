package com.angel.fym.services;

import com.angel.fym.entity.Client;
import com.angel.fym.entity.Token;
import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.services.Base.BaseService;
import com.angel.fym.util.FileManager;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;

public class AuthorizeService extends BaseService implements BaseExecutable {
    private FileManager fileManager;
    private boolean isValid;

    public AuthorizeService(Request request, Response response) {
        super(request, response);
        this.fileManager = new FileManager();
        this.isValid = false;
    }

    public boolean isValid() {
        return this.isValid;
    }

    @Override
    public String action() {
        this.doTokenValidation();

        if (!this.isValid) {
            this.unauthorizedResponse();
            return this.responseMessage;
        }

        return "SUCCESS_LOGIN";
    }

    private boolean doTokenValidation() {
        int[] codes = Client.CLIENT_CODES;
        String token = this.getToken();

        for (int clientId : codes) {
            if (this.validateToken(clientId, token)) {
                isValid = true;
                break;
            }
        }

        return isValid;
    }

    protected String getToken(){
        String header = this.request.headers("Authorization");

        if (header != null) {
            if (!header.contains("Bearer")) {
                return "NOT-BEARER-TOKEN";
            }

            return  header.replace("Bearer ", "");
        }

        return "NO-TOKEN-PROVIDED";
    }

    protected boolean validateToken(int clientId, String token) {
        Gson json = new Gson();
        Token tokenEntity = json.fromJson(this.fileManager.getContentFromFile("token/" + clientId + ".json"), Token.class);
        String savedToken = tokenEntity.getToken();

        if (savedToken.equals(token)){
            return true;
        }

        return false;
    }
}
