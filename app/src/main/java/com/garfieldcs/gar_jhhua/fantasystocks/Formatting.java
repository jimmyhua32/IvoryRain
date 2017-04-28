package com.garfieldcs.gar_jhhua.fantasystocks;

import java.math.BigDecimal;

public class Formatting {
    public static final double ONE_DECIMAL = 10.0;
    public static final double TWO_DECIMAL = 100.0;

    //Add more methods later
    public static Double toDecimal(BigDecimal value) {
        return Math.round((value.floatValue() * 100)) / 100.0;
    }
}
