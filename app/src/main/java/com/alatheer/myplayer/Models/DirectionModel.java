package com.alatheer.myplayer.Models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Created by elashry on 01/07/2018.
 */

public class DirectionModel implements Serializable {

    @SerializedName("routes")
    List<RouteObject> routes;

    public List<RouteObject> getRoutes() {
        return routes;
    }

    public class RouteObject
    {
        @SerializedName("overview_polyline")
        private PolyLineObject polyLineObject;
        @SerializedName("legs")
        private List<LegsObject> legs;

        public PolyLineObject getPolyLineObject() {
            return polyLineObject;
        }

        public List<LegsObject> getLegs() {
            return legs;
        }
    }

    public class LegsObject
    {
        @SerializedName("distance")
        private DistanceObject distanceObject;
        @SerializedName("duration")
        private DurationObject durationObject;
        @SerializedName("end_address")
        private String end_address;

        public DistanceObject getDistanceObject() {
            return distanceObject;
        }

        public DurationObject getDurationObject() {
            return durationObject;
        }

        public String getEnd_address() {
            return end_address;
        }
    }

    public class DistanceObject
    {
        private String value;

        public String getValue() {
            return value;
        }
    }
    public class DurationObject
    {
        private String value;

        public String getValue() {
            return value;
        }
    }
    public class PolyLineObject
    {
        private String points;

        public String getPoints() {
            return points;
        }
    }
}
