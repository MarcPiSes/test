package cat.udg.tfg.gui.http;

public class StatusResponseError extends Exception {
    private int code;
    public StatusResponseError(int responseCode) {
        code = responseCode;
    }

    public int getCode() {
        return code;
    }
}
