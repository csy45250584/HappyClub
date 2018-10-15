package com.haokuo.happyclub.util.utilscode;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by zjf on 2018/10/13.
 */
public class DecimalUtils {
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("¥0.00");

    public static String getMoneyString(double money) {
        return DECIMAL_FORMAT.format(BigDecimal.valueOf(money));
    }
}
