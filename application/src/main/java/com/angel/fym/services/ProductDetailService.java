package com.angel.fym.services;

import com.angel.fym.mapper.ProductMapper;
import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.services.Base.BaseService;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;

public class ProductDetailService extends BaseService implements BaseExecutable {
    private String productCode;

    public ProductDetailService(Request request, Response response) {
        super(request, response);
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String action() {
        ProductMapper productMapper = new ProductMapper(true);

        JsonObject product = productMapper.getProductByCode(this.productCode);

        if (this.productCode == null || product == null) {
            this.invalidResponse();
            return this.responseMessage;
        }

        this.correctResponse(product);
        return this.responseMessage;
    }
}
