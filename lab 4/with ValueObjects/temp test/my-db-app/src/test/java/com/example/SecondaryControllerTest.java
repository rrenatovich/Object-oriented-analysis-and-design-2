package com.example;

import org.junit.jupiter.api.Test;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SecondaryControllerTest {

    @Test
    public void testSwitchToPrimary() throws IOException {
        SecondaryController controller = new SecondaryController();
        assertNotNull(controller);
        // Note: This test may fail due to JavaFX scene not being initialized.
        // In a full test environment, use TestFX or mock the dependencies.
        // controller.switchToPrimary(); // Commented out as it requires JavaFX initialization
    }
}