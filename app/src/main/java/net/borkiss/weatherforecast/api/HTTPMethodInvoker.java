package net.borkiss.weatherforecast.api;

import java.io.IOException;

public interface HTTPMethodInvoker {
    HTTPResult doGet(String url) throws IOException;
}
