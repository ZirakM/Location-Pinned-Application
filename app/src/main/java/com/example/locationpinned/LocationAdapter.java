package com.example.locationpinned;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class LocationAdapter extends ArrayAdapter<Location> implements Filterable {
    private Context context;
    private List<Location> locationModelList;
    private List<Location> mOriginalValues;

    public LocationAdapter(Context context, List<Location> locationModelList) {
        super(context, 0, locationModelList);
        this.context = context;
        this.locationModelList = locationModelList;
    }

    @Override
    public int getCount() {
        return locationModelList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Location location = getItem(position);

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.location_cell, parent, false);

        TextView address = convertView.findViewById(R.id.cellAddress);
        TextView latitude = convertView.findViewById(R.id.cellLat);
        TextView longitude = convertView.findViewById(R.id.cellLong);
        TextView id = convertView.findViewById(R.id.cellID);

        address.setText(location.getAddress());
        latitude.setText(Double.toString(location.getLatitude()));
        longitude.setText(Double.toString(location.getLongitude()));
        id.setText(Integer.toString(location.getId()));

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                // Updated with filtered values
                locationModelList = (List<Location>) results.values;

                // Notify the data with new filtered values
                notifyDataSetChanged();
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<Location> filteredArrList = new ArrayList<>();

                if (mOriginalValues == null) {
                    mOriginalValues = new ArrayList<>(locationModelList);
                }

                if (constraint == null || constraint.length() == 0) {
                    // No filter, return the original values
                    results.count = mOriginalValues.size();
                    results.values = mOriginalValues;
                } else {
                    constraint = constraint.toString().toLowerCase();
                    for (int i = 0; i < mOriginalValues.size(); i++) {
                        Location data = mOriginalValues.get(i);
                        if (data.getAddress().toLowerCase().contains(constraint.toString())) {
                            filteredArrList.add(data);
                        }
                    }

                    // Set the filtered result to return
                    results.count = filteredArrList.size();
                    results.values = filteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}
