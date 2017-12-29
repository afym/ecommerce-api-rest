package com.angel.fym.services;

import com.angel.fym.entity.Category;
import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.services.Base.BaseService;
import com.angel.fym.util.FileManager;
import com.google.gson.*;
import spark.Request;
import spark.Response;

public class CategoryService  extends BaseService implements BaseExecutable {
    private FileManager fileManager;

    public CategoryService(Request request, Response response) {
        super(request, response);
        this.fileManager = new FileManager();
    }

    public String action() {
        this.correctResponse(this.getCategories());
        return this.responseMessage;
    }

    private JsonArray getCategories() {
        String[] categories = Category.CATEGORY_CODES;
        JsonArray jsonArray = new JsonArray();
        JsonParser jsonParser = new JsonParser();

        for (String categoryCode : categories) {
            jsonArray.add(jsonParser.parse(fileManager.getContentFromFile("category/" + categoryCode + ".json")).getAsJsonObject());
        }

        return jsonArray;
    }
}
