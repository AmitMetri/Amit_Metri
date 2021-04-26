package metri.amit.testapplicationopen.repository;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.text.format.DateFormat;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import metri.amit.testapplicationopen.ApiClient;
import metri.amit.testapplicationopen.R;
import metri.amit.testapplicationopen.db.DatabaseHelper;
import metri.amit.testapplicationopen.model.ErrorData;
import metri.amit.testapplicationopen.model.RouteEntity;
import metri.amit.testapplicationopen.model.RouteInfo;
import metri.amit.testapplicationopen.model.Routes;

/**
 * Created by amitmetri on 22,April,2021
 * Repository class for fetching data from network data source.
 * Applying business rules on data source.
 */
public class RoutesRepo {
    private static RoutesRepo instance;
    private static String TAG = "RoutesRepo";
    private final MutableLiveData<List<RouteEntity>> routes = new MutableLiveData<>();
    private MutableLiveData<ErrorData> errorDataMutableLiveData = new MutableLiveData<>();

    /*
     * Singleton method for accessing repository object
     * */
    public static synchronized RoutesRepo getInstance() {
        if (instance == null) {
            instance = new RoutesRepo();
        }
        return instance;
    }

    /*
     * Helper method to check the connectivity
     * */
    public boolean isConnectedToNetwork(Context context) {
        ConnectivityManager conMgr = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo == null || !netInfo.isConnected() || !netInfo.isAvailable()) {
                return false;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error: " + e);
        }
        return true;
    }

    /*
     * Do network call and get the route details.
     * Retrofit is used along with RxJava adapter.
     * Fetch data on io thread
     * Transform data on computational thread
     * Emit data on main thread.
     * */
    public void getRoutesFromNetwork(final Context applicationContext) {

        if (routes.getValue() == null) {
            if (isConnectedToNetwork(applicationContext)) {
                ApiClient.getClient(applicationContext).create(RouteServices.class).getRoutes()
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.computation())
                        /*
                         * Map function from RxJava is used to transform the API output to required output.
                         * This code logic is running on computation thread to avoid any ANRs
                         * */
                        .map(new Function<Routes, List<RouteEntity>>() {
                            @Override
                            public List<RouteEntity> apply(Routes routes) {

                                List<RouteEntity> routeEntities = new ArrayList<>();

                                if (validateRoutesResponse(routes)) {

                                    /*
                                     * RouteEntity is the model class, which will hold the data required for UI.
                                     * RouteEntity is prepared by using API response.
                                     * There is need for improvement in the API response to avoid
                                     * extra code logic at app level
                                     * Few of the RouteEntity properties are initialized below
                                     * */
                                    for (RouteInfo routeInfo : routes.getRouteInfo()) {
                                        if (!routeInfo.getId().equalsIgnoreCase("R001"))//No data available. API response needs to be optimized to avoid extra logic at app level
                                            routeEntities.add(new RouteEntity(routeInfo.getId().toUpperCase(),
                                                    routeInfo.getSource(),
                                                    routeInfo.getDestination(),
                                                    getMinutesFromDuration(routeInfo.getTripDuration())));
                                    }

                                    /*
                                     * Remaining RouteEntity properties are initialized below
                                     * This partial initialization can avoided by improving API response
                                     * */
                                    if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR002().get(0).getTripStartTime())).contains("-")) {
                                        routeEntities.get(0).setTripStartTime(getEpochTime(routes.getRouteTimings().getR002().get(0).getTripStartTime()));
                                        routeEntities.get(0).setAvailableSeats(routes.getRouteTimings().getR002().get(0).getAvaiable());
                                        routeEntities.get(0).setTotalSeats(routes.getRouteTimings().getR002().get(0).getTotalSeats());
                                    } else if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR002().get(1).getTripStartTime())).contains("-")) {
                                        routeEntities.get(0).setTripStartTime(getEpochTime(routes.getRouteTimings().getR002().get(1).getTripStartTime()));
                                        routeEntities.get(0).setAvailableSeats(routes.getRouteTimings().getR002().get(1).getAvaiable());
                                        routeEntities.get(0).setTotalSeats(routes.getRouteTimings().getR002().get(1).getTotalSeats());
                                    } else if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR002().get(2).getTripStartTime())).contains("-")) {
                                        routeEntities.get(0).setTripStartTime(getEpochTime(routes.getRouteTimings().getR002().get(2).getTripStartTime()));
                                        routeEntities.get(0).setAvailableSeats(routes.getRouteTimings().getR002().get(2).getAvaiable());
                                        routeEntities.get(0).setTotalSeats(routes.getRouteTimings().getR002().get(2).getTotalSeats());
                                    }

                                    if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR003().get(0).getTripStartTime())).contains("-")) {
                                        routeEntities.get(1).setTripStartTime(getEpochTime(routes.getRouteTimings().getR003().get(0).getTripStartTime()));
                                        routeEntities.get(1).setAvailableSeats(routes.getRouteTimings().getR003().get(0).getAvaiable());
                                        routeEntities.get(1).setTotalSeats(routes.getRouteTimings().getR003().get(0).getTotalSeats());
                                    } else if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR003().get(1).getTripStartTime())).contains("-")) {
                                        routeEntities.get(1).setTripStartTime(getEpochTime(routes.getRouteTimings().getR003().get(1).getTripStartTime()));
                                        routeEntities.get(1).setAvailableSeats(routes.getRouteTimings().getR003().get(1).getAvaiable());
                                        routeEntities.get(1).setTotalSeats(routes.getRouteTimings().getR003().get(1).getTotalSeats());
                                    } else if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR003().get(2).getTripStartTime())).contains("-")) {
                                        routeEntities.get(1).setTripStartTime(getEpochTime(routes.getRouteTimings().getR003().get(2).getTripStartTime()));
                                        routeEntities.get(1).setAvailableSeats(routes.getRouteTimings().getR003().get(2).getAvaiable());
                                        routeEntities.get(1).setTotalSeats(routes.getRouteTimings().getR003().get(2).getTotalSeats());
                                    }


                                    if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR004().get(0).getTripStartTime())).contains("-")) {
                                        routeEntities.get(2).setTripStartTime(getEpochTime(routes.getRouteTimings().getR004().get(0).getTripStartTime()));
                                        routeEntities.get(2).setAvailableSeats(routes.getRouteTimings().getR004().get(0).getAvaiable());
                                        routeEntities.get(2).setTotalSeats(routes.getRouteTimings().getR004().get(0).getTotalSeats());
                                    } else if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR004().get(1).getTripStartTime())).contains("-")) {
                                        routeEntities.get(2).setTripStartTime(getEpochTime(routes.getRouteTimings().getR004().get(1).getTripStartTime()));
                                        routeEntities.get(2).setAvailableSeats(routes.getRouteTimings().getR004().get(1).getAvaiable());
                                        routeEntities.get(2).setTotalSeats(routes.getRouteTimings().getR004().get(1).getTotalSeats());
                                    } else if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR004().get(2).getTripStartTime())).contains("-")) {
                                        routeEntities.get(2).setTripStartTime(getEpochTime(routes.getRouteTimings().getR004().get(2).getTripStartTime()));
                                        routeEntities.get(2).setAvailableSeats(routes.getRouteTimings().getR004().get(2).getAvaiable());
                                        routeEntities.get(2).setTotalSeats(routes.getRouteTimings().getR004().get(2).getTotalSeats());
                                    }


                                    if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR005().get(0).getTripStartTime())).contains("-")) {
                                        routeEntities.get(3).setTripStartTime(getEpochTime(routes.getRouteTimings().getR005().get(0).getTripStartTime()));
                                        routeEntities.get(3).setAvailableSeats(routes.getRouteTimings().getR005().get(0).getAvaiable());
                                        routeEntities.get(3).setTotalSeats(routes.getRouteTimings().getR005().get(0).getTotalSeats());
                                    } else if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR005().get(1).getTripStartTime())).contains("-")) {
                                        routeEntities.get(3).setTripStartTime(getEpochTime(routes.getRouteTimings().getR005().get(1).getTripStartTime()));
                                        routeEntities.get(3).setAvailableSeats(routes.getRouteTimings().getR005().get(1).getAvaiable());
                                        routeEntities.get(3).setTotalSeats(routes.getRouteTimings().getR005().get(1).getTotalSeats());
                                    } else if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR005().get(2).getTripStartTime())).contains("-")) {
                                        routeEntities.get(3).setTripStartTime(getEpochTime(routes.getRouteTimings().getR005().get(2).getTripStartTime()));
                                        routeEntities.get(3).setAvailableSeats(routes.getRouteTimings().getR005().get(2).getAvaiable());
                                        routeEntities.get(3).setTotalSeats(routes.getRouteTimings().getR005().get(2).getTotalSeats());
                                    }

                                    /*
                                     * Commenting below three lines as route timing is not available for R001
                                     * */
                                /*if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR001().get(0).getTripStartTime())).contains("-")) {
                                    routeEntities.get(0).setTripStartTime(getEpochTime(routes.getRouteTimings().getR001().get(0).getTripStartTime()));
                                    routeEntities.get(0).setAvailableSeats(routes.getRouteTimings().getR001().get(0).getAvaiable());
                                    routeEntities.get(0).setTotalSeats(routes.getRouteTimings().getR001().get(0).getTotalSeats());
                                } else if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR001().get(1).getTripStartTime())).contains("-")) {
                                    routeEntities.get(0).setTripStartTime(getEpochTime(routes.getRouteTimings().getR001().get(1).getTripStartTime()));
                                    routeEntities.get(0).setAvailableSeats(routes.getRouteTimings().getR001().get(1).getAvaiable());
                                    routeEntities.get(0).setTotalSeats(routes.getRouteTimings().getR001().get(1).getTotalSeats());
                                } else if (RoutesRepo.getInstance().getRelativeTime(getEpochTime(routes.getRouteTimings().getR001().get(2).getTripStartTime())).contains("-")) {
                                    routeEntities.get(0).setTripStartTime(getEpochTime(routes.getRouteTimings().getR001().get(2).getTripStartTime()));
                                    routeEntities.get(0).setAvailableSeats(routes.getRouteTimings().getR001().get(2).getAvaiable());
                                    routeEntities.get(0).setTotalSeats(routes.getRouteTimings().getR001().get(2).getTotalSeats());
                                }*/


                                } else {
                                    errorDataMutableLiveData.setValue(
                                            new ErrorData("050", applicationContext.getString(R.string.error),
                                                    applicationContext.getString(R.string.generic_error)));
                                }
                                return routeEntities;
                            }
                        })
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new DisposableSingleObserver<List<RouteEntity>>() {
                            @Override
                            public void onSuccess(List<RouteEntity> routeEntities) {
                                DatabaseHelper.getInstance(applicationContext).insertRoutes(routeEntities);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Log.e(TAG, "Error: " + e);
                                errorDataMutableLiveData.setValue(
                                        new ErrorData("050", applicationContext.getString(R.string.error),
                                                applicationContext.getString(R.string.generic_error)));
                            }
                        });

            } else {
                errorDataMutableLiveData.setValue(
                        new ErrorData("050", applicationContext.getString(R.string.no_network),
                                applicationContext.getString(R.string.connect_to_network)));
            }
        }
    }


    /*
    * Validate the API response.
    * More validations can be added post improving of API response.
    * */
    private boolean validateRoutesResponse(Routes routes) {

        if (routes != null) {
            if (!routes.getRouteInfo().isEmpty()) {
                for (RouteInfo routeInfo : routes.getRouteInfo()) {
                    if (routeInfo.getId() == null
                            || routeInfo.getSource() == null
                            || routeInfo.getDestination() == null
                            || routeInfo.getId().isEmpty()
                            || routeInfo.getSource().isEmpty()
                            || routeInfo.getDestination().isEmpty()) {
                        return false;
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    /*
     * Get the Routes info from Room DB.
     * */
    public LiveData<List<RouteEntity>> getRoutesFromDB(Context context) {
        return DatabaseHelper.getInstance(context).fetchAllRoutes();
    }

    /*
     * As the duration in provided in both hours and minutes,
     * below helper method will convert hours into minutes and returns minutes.
     * */
    public int getMinutesFromDuration(String duration) {
        int minutes = 0;
        try {
            if (duration.contains("min")) {
                minutes = Integer.parseInt(duration.replace("min", "").trim());
            } else {
                minutes = Integer.parseInt(duration.replace("hrs", "").trim()) * 60;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return minutes;
    }

    /*
     * tripStartTime is provided in HH:mm format.
     * This helper method will convert this time to epoch time(Standard for time calculations).
     * */
    private long getEpochTime(String tripStartTime) {
        Date date = new Date();
        String monthString = (String) DateFormat.format("MMM", date); // Jun
        String day = (String) DateFormat.format("dd", date); // 20
        String hour = tripStartTime.substring(0, 2);
        String min = tripStartTime.substring(3, 5);

        long epoch = 0;
        try {
            epoch = new SimpleDateFormat("MMM dd, HH:mm")
                    .parse(monthString + " " + day + ", " + hour + ":" + min).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return epoch;
    }

    /*
     * This method converts epoch time to HH:mm format for UI purpose in the app.
     * */
    public String getTripStartTimeFromEpoch(long epochTime) {
        Date date1 = new Date(epochTime);
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, HH:mm", Locale.ENGLISH);
        return format.format(date1);
    }

    /*
     * This method converts epoch time + duration to HH:mm format for UI purpose in the app.
     * */
    public String getTripEndTimeFromEpoch(long epochTime, int duration) {
        long newEpochTime = epochTime + TimeUnit.MINUTES.toMillis(duration);
        Date date1 = new Date(newEpochTime);
        SimpleDateFormat format = new SimpleDateFormat("MMM dd, HH:mm", Locale.ENGLISH);
        return format.format(date1);
    }

    /*
     * Helper method to get how much time left for the trip
     * */
    public String getRelativeTime(long tripStartTime) {
        return findDifference(getTripStartTimeFromEpoch(tripStartTime), getTripStartTimeFromEpoch(System.currentTimeMillis()));
    }

    static String findDifference(String start_date,
                                 String end_date) {

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, HH:mm", Locale.ENGLISH);

        try {
            Date d1 = sdf.parse(start_date);
            Date d2 = sdf.parse(end_date);

            long difference_In_Time = 0;
            if (d2 != null && d1 != null) {
                difference_In_Time = d2.getTime() - d1.getTime();
            }

            long difference_In_Minutes
                    = TimeUnit
                    .MILLISECONDS
                    .toMinutes(difference_In_Time)
                    % 60;

            long difference_In_Hours
                    = TimeUnit
                    .MILLISECONDS
                    .toHours(difference_In_Time)
                    % 24;

            return difference_In_Hours + "Hr " + difference_In_Minutes + "min";
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    public LiveData<ErrorData> subscribeForError() {
        return errorDataMutableLiveData;
    }
}
