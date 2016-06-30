package net.borkiss.weatherforecast.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class WrapperSearchResultDTO implements Serializable {
    private static final long serialVersionUID = -8462313081250349827L;

    @SerializedName("list")
    List<PlaceDTO> placeDTOList;

    public List<PlaceDTO> getPlaceDTOList() {
        return placeDTOList;
    }
}
