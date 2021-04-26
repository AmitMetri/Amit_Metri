package metri.amit.testapplicationopen.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import metri.amit.testapplicationopen.model.RouteEntity;

/**
 * Created by amitmetri on 22,April,2021
 * Database access object of Room DB
 */
@Dao
public interface RouteDetailsDao {

    @Query("select * from RouteEntity")
    LiveData<List<RouteEntity>> fetchAllRoutes();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RouteEntity> routeEntities);
}
