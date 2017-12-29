package com.angel.fym.services;

import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.services.Base.BaseService;
import com.angel.fym.util.FileManager;
import spark.Request;
import spark.Response;

public class IndexService extends BaseService implements BaseExecutable {
    public IndexService(Request request, Response response) {
        super(request, response);
    }

    public String action() {
        this.htmlResponse();
        FileManager fileManager = new FileManager();
        return fileManager.getContentFromFile("index/index.html");
    }
}
