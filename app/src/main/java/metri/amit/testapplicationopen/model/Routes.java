
package metri.amit.testapplicationopen.model;

import java.util.List;

public class Routes {

    private List<RouteInfo> routeInfo = null;
    private RouteTimings routeTimings;

    public List<RouteInfo> getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(List<RouteInfo> routeInfo) {
        this.routeInfo = routeInfo;
    }

    public RouteTimings getRouteTimings() {
        return routeTimings;
    }

    public void setRouteTimings(RouteTimings routeTimings) {
        this.routeTimings = routeTimings;
    }

}
