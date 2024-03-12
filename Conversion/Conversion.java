package Conversion;

public class Conversion {
    public static long toLong(Object obj) {
        String stringToConvert = String.valueOf(obj);
        return Long.parseLong(stringToConvert);
    }
}
