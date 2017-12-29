package com.angel.fym.services;

import com.angel.fym.entity.Category;
import com.angel.fym.mapper.ProductMapper;
import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.services.Base.BaseService;
import com.angel.fym.util.FileManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import spark.Request;
import spark.Response;

public class CategoryDetailService extends BaseService implements BaseExecutable {
    private FileManager fileManager;
    private String categoryCode;

    public CategoryDetailService(Request request, Response response) {
        super(request, response);
        this.fileManager = new FileManager();
    }

    public void setCategoryCode(String categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String action() {
        ProductMapper productMapper = new ProductMapper(false);
        JsonArray array = productMapper.getProductsByCategory(this.categoryCode);

        if (array == null) {
            this.invalidResponse();
            return this.responseMessage;
        }

        this.correctResponse(array);
        return this.responseMessage;
    }
}
