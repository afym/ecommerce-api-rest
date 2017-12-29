package com.angel.fym.services;

import com.angel.fym.mapper.ProductMapper;
import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.util.FileManager;
import com.google.gson.*;
import spark.Request;
import spark.Response;


public class BasketAddItemService extends AuthorizeClientService implements BaseExecutable {
    private FileManager fileManager;

    public BasketAddItemService(Request request, Response response) {
        super(request, response);
        this.fileManager = new FileManager();
    }

    public String action() {
        boolean append = this.doAction();

        if (!append) {
            this.invalidResponse();
            return this.responseMessage;
        }

        JsonObject data = new JsonObject();
        data.addProperty("add", append);

        this.correctResponse(data);
        return this.responseMessage;
    }

    private boolean doAction() {
        String raw = this.request.body()
                .replace("\r", "")
                .replace("\n", "")
                .replace("\t", "");

        if (raw != null && !raw.equals("")) {
            try {
                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(raw).getAsJsonObject();

                if (object.get("code") != null && object.get("quantity") != null) {
                    ProductMapper productMapper = new ProductMapper(false);
                    JsonObject productDetail = productMapper.getProductByCode(object.get("code").getAsString());

                    if (productDetail != null) {
                        this.appendNewProduct(this.getBasket(), productDetail, object.get("quantity").getAsInt());
                        return true;
                    }

                }
            } catch (Exception e) {
                // TODO : logger in the future
            }
        }

        return false;
    }

    protected JsonArray getBasket() {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(fileManager.getContentFromFile(this.getBasketPath())).getAsJsonArray();
    }

    private void appendNewProduct(JsonArray basket, JsonObject product, int quantity) {
        product.remove("icon");
        product.remove("currency");
        product.addProperty("quantity", quantity);

        if (basket.size() != 0) {
            int index = 0;
            for (JsonElement basketProduct : basket) {
                if (basketProduct.getAsJsonObject().get("code").getAsString().equals(product.get("code").getAsString())) {
                    int basketQuantity = basketProduct.getAsJsonObject().get("quantity").getAsInt();
                    basketQuantity += quantity;
                    basketProduct.getAsJsonObject().addProperty("quantity", basketQuantity);
                    basket.set(index, basketProduct);
                    fileManager.setContentToFile(this.getBasketPath(), (new Gson()).toJson(basket));
                    return;
                }
                index++;
            }
        }

        basket.add(product);
        fileManager.setContentToFile(this.getBasketPath(), (new Gson()).toJson(basket));
    }

    private String getBasketPath() {
        JsonObject client = this.getClientByToken();
        int id = client.get("id").getAsInt();
        StringBuilder builder = new StringBuilder();
        return builder.append("basket/").append(id).append(".json").toString();
    }
}
