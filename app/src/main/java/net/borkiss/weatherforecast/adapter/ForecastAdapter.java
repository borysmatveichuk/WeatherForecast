package net.borkiss.weatherforecast.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.borkiss.weatherforecast.R;
import net.borkiss.weatherforecast.model.ForecastFiveDay;
import net.borkiss.weatherforecast.util.Utils;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastHolder> {

    private Context context;
    private List<ForecastFiveDay> forecastList;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ForecastAdapter(Context context, List<ForecastFiveDay> forecastList) {
        this.context = context;
        this.forecastList = forecastList;
    }

    @Override
    public ForecastHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.list_item_forecast_five_day, parent, false);
        return new ForecastHolder(view);
    }

    @Override
    public void onBindViewHolder(ForecastHolder holder, int position) {
        holder.bind(forecastList.get(position));
    }

    @Override
    public int getItemCount() {
        return forecastList.size();
    }

    public void setForecastList(List<ForecastFiveDay> forecastList) {
        this.forecastList = forecastList;
    }

    public class ForecastHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtTime;
        private ImageView imgWeatherIcon;
        private TextView txtTemperature;

        public ForecastHolder(View itemView) {
            super(itemView);

            txtTime = (TextView) itemView.findViewById(R.id.txtTime);
            imgWeatherIcon = (ImageView) itemView.findViewById(R.id.imgWeatherIcon);
            txtTemperature = (TextView) itemView.findViewById(R.id.txtTemperature);
        }

        public void bind(ForecastFiveDay forecast) {
            txtTime.setText(Utils.formatTime(forecast.getTime()));
            imgWeatherIcon.setImageResource(Utils.getIconResourceForWeatherCondition(
                    forecast.getWeatherConditionId()));
            txtTemperature.setText(Utils.formatTemperature(context, forecast.getTemperature()));
        }

        @Override
        public void onClick(View view) {

        }
    }
}
