package com.example.werkstuk;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    private TimeInstance timeInstance1;
    @Before
    public void setUp(){
        timeInstance1 = new TimeInstance(new boolean[]{true,true,true,true,true,false,true});
    }
    @Test
    public void testGetDaysStr(){
        assertEquals( "weekdays, su",timeInstance1.getDaysStrWithStr());
    }

}