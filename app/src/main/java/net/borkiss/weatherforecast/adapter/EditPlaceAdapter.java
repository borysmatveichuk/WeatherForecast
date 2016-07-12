package net.borkiss.weatherforecast.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.model.Place;

import java.util.List;

public class EditPlaceAdapter extends PlaceAdapter {

    public EditPlaceAdapter(Context context, List<Place> places) {
        super(context, places);
    }

    @Override
    public EditPlaceHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.list_item_edit_place, parent, false);
        return new EditPlaceHolder(view);
    }

    public class EditPlaceHolder extends PlaceHolder {

        private Button delete;

        public EditPlaceHolder(View itemView) {
            super(itemView);
            delete = (Button) itemView.findViewById(R.id.btnDelete);
            delete.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }
    }
}
