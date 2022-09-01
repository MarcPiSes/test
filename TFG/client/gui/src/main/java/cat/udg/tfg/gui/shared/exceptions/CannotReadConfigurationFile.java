package cat.udg.tfg.gui.shared.exceptions;

import java.io.IOException;

public class CannotReadConfigurationFile extends Exception {
    public CannotReadConfigurationFile(IOException e) {
        super(e);
    }
}
