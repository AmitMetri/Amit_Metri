package metri.amit.testapplicationopen.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by amitmetri on 22,April,2021
 */
@Entity
public class RouteEntity {

    @PrimaryKey
    @NonNull
    private String id;
    private String source;
    private String destination;
    private int tripDuration;
    private int totalSeats;
    private int availableSeats;
    private long tripStartTime;

    @Ignore
    public RouteEntity(String id, String source, String destination, int tripDuration) {
        this.id = id;
        this.source = source;
        this.destination = destination;
        this.tripDuration = tripDuration;
    }

    public RouteEntity() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public int getTripDuration() {
        return tripDuration;
    }

    public void setTripDuration(int tripDuration) {
        this.tripDuration = tripDuration;
    }

    public int getTotalSeats() {
        return totalSeats;
    }

    public void setTotalSeats(int totalSeats) {
        this.totalSeats = totalSeats;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public long getTripStartTime() {
        return tripStartTime;
    }

    public void setTripStartTime(long tripStartTime) {
        this.tripStartTime = tripStartTime;
    }

}
