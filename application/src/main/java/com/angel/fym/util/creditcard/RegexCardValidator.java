package com.angel.fym.util.creditcard;

public class RegexCardValidator {
    public static CardValidationResult isValid(final String cardIn) {
        String card = cardIn.replaceAll("[^0-9]+", ""); // remove all non-numerics
        if ((card == null) || (card.length() < 13) || (card.length() > 19)) {
            return new CardValidationResult(card,"failed length check");
        }

        if (!luhnCheck(card)) {
            return new CardValidationResult(card,"failed luhn check");
        }

        CardCompany cc = CardCompany.gleanCompany(card);
        if (cc == null) return new CardValidationResult(card,"failed card company check");

        return new CardValidationResult(card,cc);
    }

    protected static boolean luhnCheck(String cardNumber) {
        // number must be validated as 0..9 numeric first!!
        int digits = cardNumber.length();
        int oddOrEven = digits & 1;
        long sum = 0;
        for (int count = 0; count < digits; count++) {
            int digit = 0;
            try {
                digit = Integer.parseInt(cardNumber.charAt(count) + "");
            } catch(NumberFormatException e) {
                return false;
            }

            if (((count & 1) ^ oddOrEven) == 0) { // not
                digit *= 2;
                if (digit > 9) {
                    digit -= 9;
                }
            }
            sum += digit;
        }

        return (sum == 0) ? false : (sum % 10 == 0);
    }
}
