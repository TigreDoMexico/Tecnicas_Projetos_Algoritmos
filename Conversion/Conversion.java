package Conversion;

public class Conversion {
    public static long toLong(Object obj) {
        String stringToConvert = String.valueOf(obj);
        Long convertedLong = Long.parseLong(stringToConvert);
        return convertedLong;
    }
}
