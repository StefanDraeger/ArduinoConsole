package draegerit.de.arduinoconsole.util;

public enum EParity {
    NONE(0), ODD(1), EVEN(2), MARK(3), SPACE(4);

    int value = 0;

    EParity(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public static EParity getByName(String value) {
        EParity parity = null;
        for (EParity p : values()) {
            if (p.toString().equalsIgnoreCase(value)) {
                parity = p;
                break;
            }
        }
        return parity;
    }
}
