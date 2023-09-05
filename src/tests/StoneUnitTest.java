package tests;

import entities.Stone;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

public class StoneUnitTest {
    @Test
    public void testEqualsSame() {
        Stone s1 = Stone.WHITE;
        Stone s2 = Stone.WHITE;
        assertEquals(s1, s2);
    }

    @Test
    public void testEqualsDifferent() {
        Stone s1 = Stone.WHITE;
        Stone s2 = Stone.BLACK;
        assertNotEquals(s1, s2);
    }

    @Test
    public void testEqualsNull() {
        Stone s1 = Stone.WHITE;
        Stone s2 = null;
        assertNotEquals(s1, s2);
    }

    @Test
    public void testGetOpposite() {
        assertEquals(Stone.WHITE.getOpposite(), Stone.BLACK);
    }

    @Test
    public void testGetColor1() {
        assertEquals(Stone.WHITE.getColor(), Color.WHITE);
    }

    @Test
    public void testGetColor2() {
        assertEquals(Stone.BLACK.getColor(), Color.BLACK);
    }
}
