package net.borkiss.weatherforecast.dto;

import net.borkiss.weatherforecast.model.Place;

import java.util.ArrayList;
import java.util.List;

public enum DTOFactory {
     INSTANCE;

    public Place createPlace(PlaceDTO dto) {
        if (dto == null)
            return null;

        Place place = new Place();
        place.setId(dto.getId());
        place.setName(dto.getName());
        place.setCountry(dto.getCountry());
        place.setLatitude(dto.getLatitude());
        place.setLongitude(dto.getLongitude());

        return place;
    }

    public List<Place> createPlaces(List<PlaceDTO> dtoList) {
        if (dtoList == null)
            return null;

        List<Place> list = new ArrayList<>();
        for (PlaceDTO dto : dtoList) {
            list.add(createPlace(dto));
        }

        return list;
    }
}
