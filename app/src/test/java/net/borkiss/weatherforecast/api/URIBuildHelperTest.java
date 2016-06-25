package net.borkiss.weatherforecast.api;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static junit.framework.Assert.assertEquals;

@RunWith( PowerMockRunner.class )
@PrepareForTest( URIBuildHelper.class )

public class URIBuildHelperTest {

    @Test
    public void testSomething() {
        assertEquals("http://api.openweathermap.org/", URIBuildHelper.createUriCurrentWeather(123));
    }

}