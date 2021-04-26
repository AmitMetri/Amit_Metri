package metri.amit.testapplicationopen.db;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import metri.amit.testapplicationopen.model.RouteEntity;

import static metri.amit.testapplicationopen.Constants.DB_ROUTE_DETAILS;
import static metri.amit.testapplicationopen.Constants.DB_VERSION;

/**
 * Created by amitmetri on 22,April,2021
 * Room DB implementation
 */
@Database(entities = {RouteEntity.class}, version = DB_VERSION, exportSchema = false)
public abstract class RoutesRoomDb extends RoomDatabase {

    private static RoutesRoomDb dbInstance;
    private static String TAG = "RoutesRoomDb";

    /*
     * Singleton instance of Room DB
     * */
    static synchronized RoutesRoomDb getDbInstance(Context applicationContext) {
        if (dbInstance == null) {
            try {
                dbInstance = Room.databaseBuilder(applicationContext, RoutesRoomDb.class, DB_ROUTE_DETAILS)
                        .fallbackToDestructiveMigration()
                        .build();
            } catch (Exception e) {
                Log.e(TAG, "Error: " + e, e);
            }
        }
        return dbInstance;
    }

    public abstract RouteDetailsDao getRouteDetailsDao();

}
