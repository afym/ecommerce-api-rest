package com.angel.fym.services;

import com.angel.fym.entity.Client;
import com.angel.fym.entity.Token;
import com.angel.fym.services.Base.BaseService;
import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.util.FileManager;
import com.angel.fym.util.TokeGenerator;
import com.google.gson.*;
import spark.Request;
import spark.Response;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AuthorizeLoginService extends BaseService implements BaseExecutable {
    private String email;
    private String password;

    public AuthorizeLoginService(Request request, Response response) {
        super(request, response);
    }

    public String action() {
        this.setEmailAndPasswordFromRaw();
        if (this.email == null || this.password == null) {
            this.invalidResponse();
            return this.responseMessage;
        }

        Client client = this.validateClient();

        if (client == null) {
            this.unauthorizedResponse();
            return this.responseMessage;
        }

        this.correctResponse(this.getDataFromClient(client));
        this.response.status(200);
        return this.responseMessage;
    }

    private void setEmailAndPasswordFromRaw() {
        String raw = this.request.body();

        if (raw != null && !raw.equals("")) {
            try {
                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(raw).getAsJsonObject();

                if (object.get("email") != null && object.get("password") != null) {
                    this.email = object.get("email").getAsString();
                    this.password = object.get("password").getAsString();
                }
            } catch (Exception e) {
                // TODO : logger in the future
            }
        }
    }

    private Client validateClient() {
        List<Client> clients = this.getClients();
        boolean validate = false;

        for (Client client : clients) {
            String encodePassword = this.encodePassword(this.password);
            if (this.email.equals(client.getEmail()) && client.getPassword().equals(encodePassword)) {
                return client;
            }
        }

        return null;
    }

    private String encodePassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] digest = md.digest();
            return DatatypeConverter.printHexBinary(digest);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }

    private List<Client> getClients() {
        List<Client> clients = new ArrayList<>();
        FileManager fileManager = new FileManager();

        Gson json = new Gson();

        for (int clientId : Client.CLIENT_CODES) {
            clients.add(json.fromJson(fileManager.getContentFromFile("client/" + clientId + ".json"), Client.class));
        }

        return clients;
    }

    private JsonElement getDataFromClient(Client client) {
        Token token = new Token();
        token.setToken(TokeGenerator.generateToken());
        String content = (new Gson()).toJson(token);
        FileManager fileManager = new FileManager();
        fileManager.setContentToFile("token/" + client.getId() + ".json", content);

        return (new Gson().fromJson(content, JsonElement.class));
    }
}
