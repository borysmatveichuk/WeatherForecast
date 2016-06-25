package net.borkiss.weatherforecast.api;

public class HTTPResult {
    private int httpCode;
    private String content;
    private Exception exception;

    public HTTPResult() {
    }

    public int getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(int httpCode) {
        this.httpCode = httpCode;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Exception getException() {
        return exception;
    }

    public void setException(Exception exception) {
        this.exception = exception;
    }

    public String toString() {
        return "code " + httpCode + "[" + content + "]";
    }

}
