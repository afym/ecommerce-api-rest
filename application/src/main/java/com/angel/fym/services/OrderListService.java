package com.angel.fym.services;

import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.util.FileManager;
import spark.Request;
import spark.Response;

public class OrderListService extends BasketPaymentService implements BaseExecutable {
    private FileManager fileManager;

    public OrderListService(Request request, Response response) {
        super(request, response);
        this.fileManager = new FileManager();
    }

    public String action() {
        this.correctResponse(this.getOrder());
        return this.responseMessage;
    }

}
