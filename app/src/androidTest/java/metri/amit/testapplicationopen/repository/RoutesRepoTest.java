package metri.amit.testapplicationopen.repository;

import android.content.Context;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.test.platform.app.InstrumentationRegistry;
import metri.amit.testapplicationopen.model.RouteEntity;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.*;

/**
 * Created by amitmetri on 26,April,2021
 */
public class RoutesRepoTest {

    private Context appContext;

    /*
     * Instrumentation tests need application context
     * */
    @Before
    public void setUp() {
        // Context of the app under test.
        appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    /*
     * Test the return type when device is connected/NOT connected to network.
     * When device is connected to network return value expected is true.
     * When device is NOT connected to network return value expected is false.
     * */
    @Test
    public void isConnectedToNetwork() {
        boolean value = RoutesRepo.getInstance().isConnectedToNetwork(appContext);
        if(RoutesRepo.getInstance().isConnectedToNetwork(appContext))
            assertThat(value).isTrue();
        else
            assertThat(value).isFalse();
    }

    /*
     * Test the return type of getRoutesFromDB()
     * */
    @Test
    public void get() {
        LiveData<List<RouteEntity>> listLiveData = RoutesRepo.getInstance().getRoutesFromDB(appContext);
        assertThat(listLiveData).isInstanceOf(LiveData.class);
    }


    @After
    public void tearDown() {
        appContext = null;
    }

}