package campuspaths;

import campuspaths.utils.CORSFilter;
import com.google.gson.Gson;
import pathfinder.ModelConnector;
import pathfinder.datastructures.Path;
import pathfinder.datastructures.Point;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;

import java.util.*;

public class SparkServer {

    public static void main(String[] args) {
        CORSFilter corsFilter = new CORSFilter();
        corsFilter.apply();
        // The above two lines help set up some settings that allow the
        // React application to make requests to the Spark server, even though it
        // comes from a different server.
        //http://localhost:4567/find-path?from=PAR&to=CSE
        // You should leave these two lines at the very beginning of main().

        //a route that can be requested to find and returns the shortest path between two buildings
        ModelConnector model = new ModelConnector();
        Spark.get("/find-path", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                String from= request.queryParams("from");
                String to = request.queryParams("to");
                if(from == null || to == null) {
                    Spark.halt(400, "must have valid start and end");
                }
                Path<Point> path = null;
                try {
                    path = model.findShortestPath(from, to);
                } catch (IllegalArgumentException ex) {
                    return null;
                }
                Gson gson = new Gson();
                return gson.toJson(path);
            }
        });

        //a route that can be requested to get a list of buildings in uw
        Spark.get("/buildings", new Route() {
            @Override
            public Object handle(Request request, Response response) throws Exception {
                Map<String, String> map = model.buildingNames();
                int size = map.size();
                String[][] list = new String[size][2];
                int index = 0;
                List<String> set = new ArrayList<>(map.keySet());
                Collections.sort(set);
                for(String s: set) {
                    list[index][0] = s;
                    list[index][1] = map.get(s);
                    index++;
                }
                Gson gson = new Gson();
                return gson.toJson(list);
            }
        });
    }
}
