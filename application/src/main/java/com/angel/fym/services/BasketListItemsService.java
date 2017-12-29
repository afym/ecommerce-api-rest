package com.angel.fym.services;

import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.util.FileManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;


public class BasketListItemsService extends BasketAddItemService implements BaseExecutable {
    private FileManager fileManager;

    public BasketListItemsService(Request request, Response response) {
        super(request, response);
        this.fileManager = new FileManager();
    }

    public String action() {
        this.correctResponse(this.getBasketDetail());
        return this.responseMessage;
    }

    protected JsonObject getBasketDetail() {
        JsonArray basket = this.getBasket();
        JsonObject data = new JsonObject();
        data.add("products", basket);
        double total = 0.0d;

        for (JsonElement product : basket) {
            JsonObject basketProduct = product.getAsJsonObject();
            total += (basketProduct.get("quantity").getAsInt() * basketProduct.get("price").getAsDouble());
        }

        data.addProperty("total", Math.round(total * 100.0) / 100.0);

        return data;
    }
}
