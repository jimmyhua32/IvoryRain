package com.garfieldcs.gar_jhhua.fantasystocks.widget;

import java.math.BigDecimal;

public class Formatting {
    public static final double ONE_DECIMAL = 10.0;
    public static final double TWO_DECIMAL = 100.0;

    //Add more methods later
    public static Double toDecimal(BigDecimal value, double placeValue) {
        return Math.round((value.floatValue() * placeValue)) / placeValue;
    }

    public static Double toDecimal(Double value, double placeValue) {
        return Math.round((value.floatValue() * placeValue)) / placeValue;
    }
}
