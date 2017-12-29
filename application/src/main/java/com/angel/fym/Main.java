package com.angel.fym;

import com.angel.fym.services.*;
import spark.Spark;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {

        Spark.staticFiles.location("/public");

        get("/", ((request, response) -> {
            IndexService indexService = new IndexService(request, response);
            return indexService.action();
        }));

        post("/authorization/login", (request, response) -> {
            AuthorizeLoginService loginService = new AuthorizeLoginService(request, response);
            return loginService.action();
        });

        delete("/authorization/logout", (request, response) -> {
            AuthorizeService authorizeService = new AuthorizeService(request, response);
            String validate = authorizeService.action();

            if (!authorizeService.isValid()) {
                return validate;
            }

            AuthorizeLogoutService authorizeLogoutService = new AuthorizeLogoutService(request, response);
            return authorizeLogoutService.action();
        });

        get("/authorization/client", (request, response) -> {
            AuthorizeService authorizeService = new AuthorizeService(request, response);
            String validate = authorizeService.action();

            if (!authorizeService.isValid()) {
                return validate;
            }

            AuthorizeClientService authorizeClientService = new AuthorizeClientService(request, response);
            return authorizeClientService.action();
        });

        get("/category", (request, response) -> {
            AuthorizeService authorizeService = new AuthorizeService(request, response);
            String validate = authorizeService.action();

            if (!authorizeService.isValid()) {
                return validate;
            }

            CategoryService categoryService = new CategoryService(request, response);
            return categoryService.action();
        });

        get("/category/:code", (request, response) -> {
            AuthorizeService authorizeService = new AuthorizeService(request, response);
            String validate = authorizeService.action();

            if (!authorizeService.isValid()) {
                return validate;
            }

            CategoryDetailService categoryService = new CategoryDetailService(request, response);
            categoryService.setCategoryCode(request.params(":code"));
            return categoryService.action();
        });

        get("/product/:code", (request, response) -> {
            AuthorizeService authorizeService = new AuthorizeService(request, response);
            String validate = authorizeService.action();

            if (!authorizeService.isValid()) {
                return validate;
            }

            ProductDetailService productDetailService = new ProductDetailService(request, response);
            productDetailService.setProductCode(request.params(":code"));
            return productDetailService.action();
        });

        put("/basket", (request, response) -> {
            AuthorizeService authorizeService = new AuthorizeService(request, response);
            String validate = authorizeService.action();

            if (!authorizeService.isValid()) {
                return validate;
            }

            BasketAddItemService basketAddItemService = new BasketAddItemService(request, response);
            return basketAddItemService.action();
        });

        get("/basket", (request, response) -> {
            AuthorizeService authorizeService = new AuthorizeService(request, response);
            String validate = authorizeService.action();

            if (!authorizeService.isValid()) {
                return validate;
            }

            BasketListItemsService basketListItemsService = new BasketListItemsService(request, response);
            return basketListItemsService.action();
        });

        delete("/basket", (request, response) -> {
            AuthorizeService authorizeService = new AuthorizeService(request, response);
            String validate = authorizeService.action();

            if (!authorizeService.isValid()) {
                return validate;
            }

            BasketDeleteItemsService basketDeleteItemsService = new BasketDeleteItemsService(request, response);
            return basketDeleteItemsService.action();
        });

        post("/basket/payment", (request, response) -> {
            AuthorizeService authorizeService = new AuthorizeService(request, response);
            String validate = authorizeService.action();

            if (!authorizeService.isValid()) {
                return validate;
            }

            BasketPaymentService basketPaymentService = new BasketPaymentService(request, response);
            return basketPaymentService.action();
        });

        get("/order", (request, response) -> {
            AuthorizeService authorizeService = new AuthorizeService(request, response);
            String validate = authorizeService.action();

            if (!authorizeService.isValid()) {
                return validate;
            }

            OrderListService orderListService = new OrderListService(request, response);
            return orderListService.action();
        });
    }
}
