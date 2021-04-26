package metri.amit.testapplicationopen.repository;

import java.util.List;

import io.reactivex.Single;
import metri.amit.testapplicationopen.model.Routes;
import retrofit2.http.GET;

/**
 * Created by amitmetri on 22,April,2021
 */
public interface RouteServices {

    /*
    * Access point ro get the route details
    * from network provided data source
    * */
    @GET("data")
    Single<Routes> getRoutes();
}
