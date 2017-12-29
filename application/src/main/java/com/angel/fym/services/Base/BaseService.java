package com.angel.fym.services.Base;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import spark.Request;
import spark.Response;

public class BaseService {
    protected Request request;
    protected Response response;
    protected String responseMessage = "{\"ok\" : true}";

    public BaseService(Request request, Response response) {
        this.request = request;
        this.response = response;
    }

    protected Response unauthorizedResponse() {
        BaseResponse response = new BaseResponse();
        response.setMessage("You are unauthorized for this resource");
        response.setStatus(401);
        response.setError(true);

        this.response.status(401);
        this.response.type("application/json");
        this.responseMessage = (new Gson()).toJson(response);
        return this.response;
    }

    protected Response invalidResponse() {
        BaseResponse response = new BaseResponse();
        response.setMessage("Your request is invalid. Please check the documentation");
        response.setStatus(400);
        response.setError(true);

        this.response.status(400);
        this.response.type("application/json");
        this.responseMessage = (new Gson()).toJson(response);
        return this.response;
    }

    protected Response correctResponse(JsonElement data) {
        BaseResponse response = new BaseResponse();
        response.setMessage("ok");
        response.setError(false);
        response.setStatus(200);
        response.setData(data);

        this.response.status(200);
        this.response.type("application/json");
        this.responseMessage = (new Gson()).toJson(response);
        return this.response;
    }

    protected Response htmlResponse() {
        this.response.status(200);
        this.response.type("text/html");
        return this.response;
    }
}
