package draegerit.de.arduinoconsole.util;

public class USBConfiguration {

    private int baudrate;

    private int dataBits;

    private int stopbits;

    private int parity;

    public USBConfiguration() {

    }

    public USBConfiguration(int baudrate, int dataBits, int stopbits, int parity) {
        this();
        this.baudrate = baudrate;
        this.dataBits = dataBits;
        this.stopbits = stopbits;
        this.parity = parity;
    }

    public int getBaudrate() {
        return baudrate;
    }

    public void setBaudrate(int baudrate) {
        this.baudrate = baudrate;
    }

    public int getDataBits() {
        return dataBits;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopbits() {
        return stopbits;
    }

    public void setStopbits(int stopbits) {
        this.stopbits = stopbits;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }
}
