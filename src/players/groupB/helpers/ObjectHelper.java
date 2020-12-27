package players.groupB.helpers;

public class ObjectHelper {

    public static int getIntValue(Object obj) {
        if (obj instanceof Integer) {
            return (int) obj;
        }
        return 0;
    }

    public static String getStringValue(Object obj) {
        if (obj instanceof String) {
            return obj.toString();
        }
        return null;
    }

    public static double getDoubleValue(Object obj) {
        if (obj instanceof Double) {
            return (double) obj;
        }
        return 0.0;
    }
}
