package MessageUtils;

public class Header {
    /**
     * 数据长度（不包含首部）
     */
    private int dataLength;

    private byte type = 0;

    public static byte HEART_BEAT_REQUEST = 1;
    public static byte RPC_REQUEST = 2;
    public static byte RPC_RESPONSE = 3;
    public static byte REGISTRY_NORMALCONFIG=4;
    public static byte REGISTRY_TOKEN=5;
    public static byte REGISTRY_SERVICE=6;

    public Header(int dataLength, byte type) {
        this.dataLength = dataLength;
        this.type = type;
    }

    public int getDataLength() {
        return dataLength;
    }

    public void setDataLength(int dataLength) {
        this.dataLength = dataLength;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }
}