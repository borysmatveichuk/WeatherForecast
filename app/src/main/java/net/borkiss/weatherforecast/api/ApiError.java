package net.borkiss.weatherforecast.api;

public enum ApiError {

    IO_ERROR, SERVER_ERROR, CANCELLED;

    private String message;

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

}
