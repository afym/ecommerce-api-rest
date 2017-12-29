package com.angel.fym.services;

import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.services.BasketAddItemService;
import com.angel.fym.util.FileManager;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import spark.Request;
import spark.Response;


public class BasketDeleteItemsService extends BasketAddItemService implements BaseExecutable {
    private FileManager fileManager;

    public BasketDeleteItemsService(Request request, Response response) {
        super(request, response);
        this.fileManager = new FileManager();
    }

    public String action() {
        this.doAction();

        JsonObject data = new JsonObject();
        data.addProperty("clean", true);

        this.correctResponse(data);
        return this.responseMessage;
    }

    private void doAction() {
        JsonObject client = this.getClientByToken();
        int id = client.get("id").getAsInt();
        StringBuilder builder = new StringBuilder();
        builder.append("basket/").append(id).append(".json");
        fileManager.setContentToFile(builder.toString(), "[]");
    }
}
