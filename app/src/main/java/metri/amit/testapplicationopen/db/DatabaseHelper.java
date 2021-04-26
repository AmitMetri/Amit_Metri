package metri.amit.testapplicationopen.db;

import android.content.Context;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import metri.amit.testapplicationopen.model.RouteEntity;

/**
 * Created by amitmetri on 22,April,2021
 * Database helper creates single instance of RoomDB.
 * Access points to DB operations are placed here.
 */
public class DatabaseHelper {

    private static final String TAG = "DatabaseHelper";
    private static DatabaseHelper databaseHelper;
    private final RouteDetailsDao routeDetailsDao;

    /*
    * Creates database access object
    * */
    private DatabaseHelper(Context context) {
        routeDetailsDao = RoutesRoomDb.getDbInstance(context).getRouteDetailsDao();
    }

    /*
    * Create a singleton object of database helper.
    * */
    public static DatabaseHelper getInstance(Context context) {
        synchronized (DatabaseHelper.class) {
            if (databaseHelper == null) {
                databaseHelper = new DatabaseHelper(context);
                Log.d(TAG, "NEW DB OBJECT CREATED");
            }
        }
        return databaseHelper;
    }

    /*
    * Insert routes into table
    * */
    public void insertRoutes(List<RouteEntity> routeEntities) {
        new Thread(() -> {
            Log.d(TAG, "Inserting comment...");
            routeDetailsDao.insertAll(routeEntities);
        }).start();
    }

    /*
    * Observe LiveData from the table.
    * */
    public LiveData<List<RouteEntity>> fetchAllRoutes() {
        return routeDetailsDao.fetchAllRoutes();
    }
}
