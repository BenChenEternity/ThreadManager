package ajie.wiki.util;

import java.text.DecimalFormat;

public class DecimalFormatter {
    public static String setPrecision(double value, int precision) {
        DecimalFormat decimalFormat = new DecimalFormat("#.##");
        return decimalFormat.format(value);
    }
}
