package metri.amit.testapplicationopen.view;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import metri.amit.testapplicationopen.databinding.ItemRouteBinding;
import metri.amit.testapplicationopen.model.RouteEntity;
import metri.amit.testapplicationopen.repository.RoutesRepo;

/*
 * Adapter class to display the routes in recycler view
 * */
public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ViewHolder> {

    private static final String TAG = "RoutesAdapter";
    private final List<RouteEntity> routeEntities;

    public RoutesAdapter(List<RouteEntity> routeEntities) {
        this.routeEntities = routeEntities;
    }

    public void updateList(List<RouteEntity> routeEntities) {
        Log.d(TAG, "Updated list");
        this.routeEntities.clear();
        this.routeEntities.addAll(routeEntities);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RoutesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRouteBinding itemRouteBinding = ItemRouteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(itemRouteBinding);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RoutesAdapter.ViewHolder holder, int position) {
        RouteEntity routeEntity = routeEntities.get(position);

        holder.binding.textViewSource.setText(routeEntity.getSource());
        holder.binding.textViewDestination.setText(routeEntity.getDestination());
        holder.binding.textViewStartTime.setText(RoutesRepo.getInstance().getTripStartTimeFromEpoch(routeEntity.getTripStartTime()));
        holder.binding.textViewDestinationTime
                .setText(RoutesRepo.getInstance().getTripEndTimeFromEpoch(routeEntity.getTripStartTime(), routeEntity.getTripDuration()));
        holder.binding.textViewTravelTime.setText("Travel time: " + routeEntity.getTripDuration() + " min");
        holder.binding.TextViewRelativeTime.setText(RoutesRepo.getInstance().getRelativeTime(routeEntity.getTripStartTime()).replaceAll("-", ""));
        holder.binding.textViewTotalSeats.setText("Total seats: " + routeEntity.getTotalSeats());
        holder.binding.textViewAvailableSeats.setText("Available seats: " + routeEntity.getAvailableSeats());
    }

    @Override
    public int getItemCount() {
        return routeEntities.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        final ItemRouteBinding binding;

        private ViewHolder(ItemRouteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
