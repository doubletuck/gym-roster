package com.gym.roster.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class DivisionTest {

    @Test
    public void findWhenInputIsEmpty() {
        assertNull(Division.find(""), "Empty string");
        assertNull(Division.find("     "), "Multiple empty spaces");
    }

    @Test
    public void findWhenInputIsNull() {
        assertNull(Division.find(null));
    }

    @Test
    public void findDiv1() {
        assertEquals(Division.DIV1, Division.find("DIV1"), "Uppercase");
        assertEquals(Division.DIV1, Division.find("div1"), "Lowercase");
        assertEquals(Division.DIV1, Division.find(" DIV1 "), "White space");
    }

    @Test
    public void findDiv2() {
        assertEquals(Division.DIV2, Division.find("DIV2"), "Uppercase");
        assertEquals(Division.DIV2, Division.find("div2"), "Lowercase");
        assertEquals(Division.DIV2, Division.find(" DIV2 "), "White space");
    }

    @Test
    public void findDiv3() {
        assertEquals(Division.DIV3, Division.find("DIV3"), "Uppercase");
        assertEquals(Division.DIV3, Division.find("div3"), "Lowercase");
        assertEquals(Division.DIV3, Division.find(" DIV3 "), "White space");
    }
}
