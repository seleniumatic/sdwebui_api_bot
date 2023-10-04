package com.seleniumatic.sd;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.seleniumatic.sd.common.AppConfig;

public class AppConfigTest {
    @Test
    public void shouldAnswerWithTrue() {
        assertTrue( true );
    }

    @Test
    public void testLoadDefaultProperty() {
        Integer interval_sec = AppConfig.getApiRequestIntervalSeconds();
        assertEquals(Integer.valueOf(10), interval_sec);
    }
}
