package players.groupB.helpers;

import players.optimisers.ParameterSet;

public class ObjectHelper {

    public static void setValue(String name, Object obj, ParameterSet parameterSet) {
        parameterSet.setParameterValue(name, obj);
    }

    public static void setStringValue(String name, Object obj, ParameterSet parameterSet) {
        if (obj instanceof String) {
            parameterSet.setParameterValue(name, obj);
        }
    }

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
