package metri.amit.testapplicationopen.repository;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertEquals;


/**
 * Created by amitmetri on 26,April,2021
 */
public class RoutesRepoTest {

    @Test
    public void getMinutesFromDuration() {
        int result = RoutesRepo.getInstance().getMinutesFromDuration("2min");
        assertEquals(2, result);
    }

    @Test
    public void getMinutesFromDuration_1() {
        int result = RoutesRepo.getInstance().getMinutesFromDuration("2hrs");
        assertEquals(120, result);
    }

    @Rule
    public final ExpectedException exception = ExpectedException.none();

    @Test
    public void getMinutesFromDuration_2() {
        exception.expect(NumberFormatException.class);
        RoutesRepo.getInstance().getMinutesFromDuration("2 HRS");
    }

}