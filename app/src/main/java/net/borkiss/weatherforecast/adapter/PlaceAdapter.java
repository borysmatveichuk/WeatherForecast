package net.borkiss.weatherforecast.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.dto.PlaceDTO;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceHolder> {

    private Context context;
    private List<PlaceDTO> places;


    public PlaceAdapter(Context context, List<PlaceDTO> places) {
        this.context = context;
        this.places = places;
    }

    @Override
    public PlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_place, parent, false);
        return new PlaceHolder(view);
    }

    @Override
    public void onBindViewHolder(PlaceHolder holder, int position) {
        holder.bind(places.get(position));
    }

    @Override
    public int getItemCount() {
        return places.size();
    }

    public static class PlaceHolder extends RecyclerView.ViewHolder {

        private PlaceDTO place;

        private TextView placeName;
        private TextView placeCountry;

        public PlaceHolder(View itemView) {
            super(itemView);

            placeName = (TextView) itemView.findViewById(R.id.txtPlaceName);
            placeCountry = (TextView) itemView.findViewById(R.id.txtPlaceCountry);
        }

        public void bind(PlaceDTO place) {
            this.place = place;

            placeName.setText(place.getName());
            placeCountry.setText(place.getCountry());
        }
    }
}
