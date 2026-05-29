package models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class FuelInventoryTest {

    @Test
    void gettersReflectConstructorValues() {
        FuelInventory inventory = new FuelInventory(90.5, 500, true);

        assertEquals(90.5, inventory.getPrice());
        assertEquals(500, inventory.getQuantityLiters());
        assertTrue(inventory.isAvailable());
    }

    @Test
    void settersUpdateState() {
        FuelInventory inventory = new FuelInventory(90.5, 500, true);

        inventory.setPrice(88.0);
        inventory.setQuantityLiters(120);
        inventory.setAvailable(false);

        assertEquals(88.0, inventory.getPrice());
        assertEquals(120, inventory.getQuantityLiters());
        assertFalse(inventory.isAvailable());
    }
}
