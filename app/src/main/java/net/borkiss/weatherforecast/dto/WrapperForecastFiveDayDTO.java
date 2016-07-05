package net.borkiss.weatherforecast.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WrapperForecastFiveDayDTO implements Serializable {
    private static final long serialVersionUID = -5759683887215002696L;

    @SerializedName("city")
    private City city;

    @SerializedName("list")
    private List<ForecastFiveDayDTO> forecastList;

    public List<ForecastFiveDayDTO> getForecastList() {
        for (ForecastFiveDayDTO dto : forecastList) {
            dto.setCityId(getCitiId());
        }
        return forecastList;
    }

    public int getCitiId() {
        return city.cityId;
    }

    private class City {
        @SerializedName("id")
        int cityId;
        String name;
        String country;
    }
}
