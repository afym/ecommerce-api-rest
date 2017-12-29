package com.angel.fym.services;

import com.angel.fym.services.Base.BaseExecutable;
import com.angel.fym.util.FileManager;
import com.angel.fym.util.TokeGenerator;
import com.angel.fym.util.creditcard.CardValidationResult;
import com.angel.fym.util.creditcard.RegexCardValidator;
import com.google.gson.*;
import spark.Request;
import spark.Response;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BasketPaymentService extends BasketListItemsService implements BaseExecutable {
    private FileManager fileManager;
    private JsonObject data;

    public BasketPaymentService(Request request, Response response) {
        super(request, response);
        this.fileManager = new FileManager();
        this.data = new JsonObject();
        this.data.addProperty("payment", false);
    }

    public String action() {
        int isValid = this.doAction();

        if (isValid == 3) {
            this.invalidResponse();
            return  this.responseMessage;
        }

        this.correctResponse(this.data);
        return this.responseMessage;
    }

    private int doAction() {
        String raw = this.request.body();

        if (raw != null && !raw.equals("")) {
            try {
                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(raw).getAsJsonObject();

                if (object.get("type") != null && object.get("card") != null
                    && object.get("cvv") != null && object.get("expiration") != null
                    && object.get("phone") != null && object.get("country") != null
                    && object.get("city") != null && object.get("shipping") != null   ) {

                    if (!this.validateCreditCard(object.get("card").getAsString())
                            || !this.validateCreditCardType(object.get("type").getAsString())
                            || !this.validateExpiration(object.get("expiration").getAsString())
                            || !this.validatePhone(object.get("phone").getAsString())
                            || !this.validateCvv(object.get("cvv").getAsString())) {
                        return 1;
                    }

                    Thread.sleep(3500);
                    String token = TokeGenerator.generateToken().substring(5);
                    this.data.addProperty("message" , "Your payment was correct");
                    this.data.addProperty("payment", true);
                    this.data.addProperty("order", token);
                    this.appendOrder(token);
                    return 0;
                }
                this.data.addProperty("message" , "Your information is not complete");
            } catch (Exception e) {
                // TODO : logger in the future
            }
        }

        return 3;
    }

    private boolean validateCreditCard(String creditCard) {
        CardValidationResult result = RegexCardValidator.isValid(creditCard);

        if (!result.isValid()) {
            this.data.addProperty("message", "Your credit card is not valid");
            return false;
        }

        return true;
    }

    private boolean validateCreditCardType(String type) {
        if (type.equals("visa") || type.equals("amex") || type.equals("master ")) {
            return true;
        }

        this.data.addProperty("message", "Your credit card type is wrong");

        return false;
    }

    private boolean validateExpiration(String expiration) {
        if (!expiration.matches("(?:0[1-9]|1[0-2])/[0-9]{2}")) {
            this.data.addProperty("message", "Your credit card expiration date is incorrect");
            return  false;
        }

        return true;
    }

    private boolean validatePhone(String phone) {
        if (!phone.matches("^\\+(?:[0-9] ?){6,14}[0-9]$")) {
            this.data.addProperty("message", "Your phone is incorrect");
            return  false;
        }

        return true;
    }

    private boolean validateCvv(String cvv) {
        if (!cvv.matches("^[0-9]{3,4}$")) {
            this.data.addProperty("message", "Your cvv code is incorrect");
            return  false;
        }

        return true;
    }

    private void appendOrder(String token) {
        JsonObject basket = this.getBasketDetail();
        JsonObject order = new JsonObject();
        order.addProperty("order", token);
        order.addProperty("status", "pending");
        order.addProperty("date", this.getDate());
        order.addProperty("amount", basket.get("total").getAsDouble());
        JsonArray orders = this.getOrder();
        orders.add(order);
        fileManager.setContentToFile(this.getOrderPath(), (new Gson()).toJson(orders));
    }

    private String getOrderPath() {
        JsonObject client = this.getClientByToken();
        int id = client.get("id").getAsInt();
        StringBuilder builder = new StringBuilder();
        return builder.append("order/").append(id).append(".json").toString();
    }

    protected JsonArray getOrder() {
        JsonParser jsonParser = new JsonParser();
        return jsonParser.parse(fileManager.getContentFromFile(this.getOrderPath())).getAsJsonArray();
    }

    private String getDate() {
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        return df.format(today);
    }
}
