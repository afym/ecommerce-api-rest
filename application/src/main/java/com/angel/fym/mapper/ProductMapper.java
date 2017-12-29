package com.angel.fym.mapper;

import com.angel.fym.entity.Product;
import com.angel.fym.util.FileManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.List;

public class ProductMapper {
    private HashMap<String, List<Product>> products;
    private boolean withDescription;
    private FileManager fileManager;

    public ProductMapper(boolean withDescription) {
        this.products = new HashMap<>();
        this.withDescription = withDescription;
        this.fileManager = new FileManager();
    }

    public JsonObject getProductByCode(String code){
        JsonObject product = null;
        HashMap<String, String[]> mapper = this.mapper();

        for(String category : mapper.keySet()) {
            String[] products = mapper.get(category);
            for (String productCode : products) {
                if (productCode.equals(code)) {
                    JsonParser jsonParser = new JsonParser();
                    StringBuilder builder = new StringBuilder();
                    builder.append("product/").append(category).append("/").append(code).append(".json");
                    product = jsonParser.parse(fileManager.getContentFromFile(builder.toString())).getAsJsonObject();
                    break;
                }
            }
        }

        return product;
    }

    public JsonArray getProductsByCategory(String category) {
        JsonArray jsonArray = new JsonArray();
        JsonParser jsonParser = new JsonParser();
        String[] codes = this.mapper().get(category);

        if (codes == null) {
            return null;
        }

        for (String code : codes) {
            StringBuilder builder = new StringBuilder();
            builder.append("product/").append(category).append("/").append(code).append(".json");
            JsonObject product = jsonParser.parse(fileManager.getContentFromFile(builder.toString())).getAsJsonObject();

            if (!this.withDescription) {
                product.remove("description");
            }

            jsonArray.add(product);
        }

        return jsonArray;
    }

    private HashMap<String, String[]> mapper() {
        HashMap<String, String[]> mapper = new HashMap<>();
        mapper.put("cat001",new String[] {"pro2001", "pro2002", "pro2003"});
        mapper.put("cat002",new String[] {"pro4004", "pro4005", "pro4006", "pro4007", "pro4008"});
        mapper.put("cat003",new String[] {"pro8006", "pro8005"});
        mapper.put("cat004",new String[] {"pro9708", "pro9709", "pro9710", "pro9711", "pro9712"});
        mapper.put("cat005",new String[] {"pro1101", "pro1102", "pro1103"});

        return mapper;
    }
}
