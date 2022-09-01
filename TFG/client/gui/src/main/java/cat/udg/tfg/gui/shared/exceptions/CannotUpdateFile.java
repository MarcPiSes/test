package cat.udg.tfg.gui.shared.exceptions;

import java.io.IOException;

public class CannotUpdateFile extends Exception {
    public CannotUpdateFile(IOException e) {
        super(e);
    }
}
