package net.borkiss.weatherforecast.api;

public interface ApiCallback<T> {
    void onSuccess(final T result);
    void onError(final ApiError error);
}
