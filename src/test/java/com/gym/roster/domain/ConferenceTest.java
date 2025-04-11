package com.gym.roster.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class ConferenceTest {

    @Test
    public void findWhenInputIsEmpty() {
        assertNull(Conference.find(""), "Empty string");
        assertNull(Conference.find("     "), "Multiple empty spaces");
    }

    @Test
    public void findWhenInputIsNull() {
        assertNull(Conference.find(null));
    }

    @Test
    public void findAcc() {
        assertEquals(Conference.ACC, Conference.find("ACC"), "Uppercase");
        assertEquals(Conference.ACC, Conference.find("acc"), "Lowercase");
        assertEquals(Conference.ACC, Conference.find(" ACC "), "White space");
    }

    @Test
    public void findBig12() {
        assertEquals(Conference.BIG12, Conference.find("BIG12"), "Uppercase");
        assertEquals(Conference.BIG12, Conference.find("big12"), "Lowercase");
        assertEquals(Conference.BIG12, Conference.find(" BIG12 "), "White space");
    }

    @Test
    public void findBigTen() {
        assertEquals(Conference.BIGTEN, Conference.find("BIGTEN"), "Uppercase");
        assertEquals(Conference.BIGTEN, Conference.find("bigten"), "Lowercase");
        assertEquals(Conference.BIGTEN, Conference.find(" BIGTEN "), "White space");
    }

    @Test
    public void findEagl() {
        assertEquals(Conference.EAGL, Conference.find("EAGL"), "Uppercase");
        assertEquals(Conference.EAGL, Conference.find("eagl"), "Lowercase");
        assertEquals(Conference.EAGL, Conference.find(" EAGL "), "White space");
    }

    @Test
    public void findGec() {
        assertEquals(Conference.GEC, Conference.find("GEC"), "Uppercase");
        assertEquals(Conference.GEC, Conference.find("gec"), "Lowercase");
        assertEquals(Conference.GEC, Conference.find(" GEC "), "White space");
    }

    @Test
    public void findInd() {
        assertEquals(Conference.IND, Conference.find("IND"), "Uppercase");
        assertEquals(Conference.IND, Conference.find("ind"), "Lowercase");
        assertEquals(Conference.IND, Conference.find(" IND "), "White space");
    }

    @Test
    public void findMac() {
        assertEquals(Conference.MAC, Conference.find("MAC"), "Uppercase");
        assertEquals(Conference.MAC, Conference.find("mac"), "Lowercase");
        assertEquals(Conference.MAC, Conference.find(" MAC "), "White space");
    }

    @Test
    public void findMic() {
        assertEquals(Conference.MIC, Conference.find("MIC"), "Uppercase");
        assertEquals(Conference.MIC, Conference.find("mic"), "Lowercase");
        assertEquals(Conference.MIC, Conference.find(" MIC "), "White space");
    }

    @Test
    public void findMpsf() {
        assertEquals(Conference.MPSF, Conference.find("MPSF"), "Uppercase");
        assertEquals(Conference.MPSF, Conference.find("mpsf"), "Lowercase");
        assertEquals(Conference.MPSF, Conference.find(" MPSF "), "White space");
    }

    @Test
    public void findMw() {
        assertEquals(Conference.MW, Conference.find("MW"), "Uppercase");
        assertEquals(Conference.MW, Conference.find("mw"), "Lowercase");
        assertEquals(Conference.MW, Conference.find(" MW "), "White space");
    }

    @Test
    public void findNcgaEast() {
        assertEquals(Conference.NCGAEAST, Conference.find("NCGAEAST"), "Uppercase");
        assertEquals(Conference.NCGAEAST, Conference.find("ncgaeast"), "Lowercase");
        assertEquals(Conference.NCGAEAST, Conference.find(" NCGAEAST "), "White space");
    }

    @Test
    public void findPac12() {
        assertEquals(Conference.PAC12, Conference.find("PAC12"), "Uppercase");
        assertEquals(Conference.PAC12, Conference.find("pac12"), "Lowercase");
        assertEquals(Conference.PAC12, Conference.find(" PAC12 "), "White space");
    }

    @Test
    public void findSec() {
        assertEquals(Conference.SEC, Conference.find("SEC"), "Uppercase");
        assertEquals(Conference.SEC, Conference.find("sec"), "Lowercase");
        assertEquals(Conference.SEC, Conference.find(" SEC "), "White space");
    }

    @Test
    public void findWiac() {
        assertEquals(Conference.WIAC, Conference.find("WIAC"), "Uppercase");
        assertEquals(Conference.WIAC, Conference.find("wiac"), "Lowercase");
        assertEquals(Conference.WIAC, Conference.find(" WIAC "), "White space");
    }
}
