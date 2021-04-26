package metri.amit.testapplicationopen.viewmodel;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import metri.amit.testapplicationopen.db.DatabaseHelper;
import metri.amit.testapplicationopen.model.ErrorData;
import metri.amit.testapplicationopen.model.RouteEntity;
import metri.amit.testapplicationopen.repository.RoutesRepo;

/**
 * Created by amitmetri on 22,April,2021
 */
public class RoutesViewModel extends AndroidViewModel {
    public RoutesViewModel(@NonNull Application application) {
        super(application);
    }

    public void getRoutesFromNetwork(){
        RoutesRepo.getInstance().getRoutesFromNetwork(getApplication().getApplicationContext());
    }

    public LiveData<List<RouteEntity>> getRoutesFromDB() {
        return RoutesRepo.getInstance().getRoutesFromDB(getApplication().getApplicationContext());
    }

    public LiveData<ErrorData> subscribeForError() {
        return RoutesRepo.getInstance().subscribeForError();
    }
}
