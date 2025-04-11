package com.gym.roster.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class RegionTest {

    @Test
    public void findWhenInputIsEmpty() {
        assertNull(Region.find(""), "Empty string");
        assertNull(Region.find("     "), "Multiple empty spaces");
    }

    @Test
    public void findWhenInputIsNull() {
        assertNull(Region.find(null));
    }

    @Test
    public void findC() {
        assertEquals(Region.C, Region.find("C"), "Uppercase");
        assertEquals(Region.C, Region.find("c"), "Lowercase");
        assertEquals(Region.C, Region.find(" C "), "White space");
    }


    @Test
    public void findNC() {
        assertEquals(Region.NC, Region.find("NC"), "Uppercase");
        assertEquals(Region.NC, Region.find("nc"), "Lowercase");
        assertEquals(Region.NC, Region.find(" NC "), "White space");
    }

    @Test
    public void findNE() {
        assertEquals(Region.NE, Region.find("NE"), "Uppercase");
        assertEquals(Region.NE, Region.find("ne"), "Lowercase");
        assertEquals(Region.NE, Region.find(" NE "), "White space");
    }

    @Test
    public void findSC() {
        assertEquals(Region.SC, Region.find("SC"), "Uppercase");
        assertEquals(Region.SC, Region.find("sc"), "Lowercase");
        assertEquals(Region.SC, Region.find(" SC "), "White space");
    }

    @Test
    public void findSE() {
        assertEquals(Region.SE, Region.find("SE"), "Uppercase");
        assertEquals(Region.SE, Region.find("se"), "Lowercase");
        assertEquals(Region.SE, Region.find(" SE "), "White space");
    }

    @Test
    public void findW() {
        assertEquals(Region.W, Region.find("W"), "Uppercase");
        assertEquals(Region.W, Region.find("w"), "Lowercase");
        assertEquals(Region.W, Region.find(" W "), "White space");
    }

    @Test
    public void findNA() {
        assertEquals(Region.NA, Region.find("NA"), "Uppercase");
        assertEquals(Region.NA, Region.find("na"), "Lowercase");
        assertEquals(Region.NA, Region.find(" NA "), "White space");
    }
}
