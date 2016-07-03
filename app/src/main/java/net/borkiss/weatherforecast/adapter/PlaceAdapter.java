package net.borkiss.weatherforecast.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.api.WeatherStation;
import net.borkiss.weatherforecast.model.Place;

import java.util.List;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlaceHolder> {

    private Context context;
    private List<Place> places;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public PlaceAdapter(Context context, List<Place> places) {
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

    public void setPlaces(List<Place> places) {
        this.places = places;
    }

    public class PlaceHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private Place place;

        private TextView placeName;
        private TextView placeCountry;
        private Button delete;

        public PlaceHolder(View itemView) {
            super(itemView);

            placeName = (TextView) itemView.findViewById(R.id.txtPlaceName);
            placeCountry = (TextView) itemView.findViewById(R.id.txtPlaceCountry);
            delete = (Button) itemView.findViewById(R.id.btnDelete);
            delete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bind(Place place) {
            this.place = place;

            placeName.setText(place.getName());
            placeCountry.setText(place.getCountry());
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }
}
