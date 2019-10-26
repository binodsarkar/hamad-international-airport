package hamad.international.airport.utils;

import android.util.Log;

import java.math.BigDecimal;
import java.math.MathContext;

import hamad.international.airport.classes.BeaconCls;

public class NavUtils {

    private double lat = 0f;
    private double lng = 0f;

    public static BeaconCls getLatLong(double min, double max, BeaconCls minBeacon, BeaconCls maxBeacon) {

        MathContext mc = new MathContext(8); // 2 precision

        try {
            BigDecimal lat1 = new BigDecimal(minBeacon.getLat());
            BigDecimal lng1 = new BigDecimal(minBeacon.getLng());

            BigDecimal lat2 = new BigDecimal(maxBeacon.getLat());
            BigDecimal lng2 = new BigDecimal(maxBeacon.getLng());

            BigDecimal lonDiff = lng1.subtract(lng2);
            BigDecimal latDiff = lat1.subtract(lat2);

            // Log.w("NavUtils: diff " , (lat1.doubleValue() - lat2.doubleValue()) + "");
            Log.w("NavUtils: diff " , lat1.doubleValue() + " -- " + lat2.doubleValue());
            BigDecimal slope = lonDiff.divide(latDiff, mc);

            Log.w("NavUtils: slope " , slope + "");


            /*double slope = (minBeacon.getLng() - maxBeacon.getLng()) / (minBeacon.getLat() - maxBeacon.getLat());

            Log.w("NavUtils: slope " , slope + "" + (minBeacon.getLng() - maxBeacon.getLng()));

            double x2 = minBeacon.getLat() + (min * Math.cos(slope));

            double y2 = minBeacon.getLng() + (min * Math.cos(slope));

            Log.w("NavUtils: slope " , x2 + " --- " + y2);*/

            double x2 = minBeacon.getLat() + (min * Math.cos(slope.doubleValue()));

            double y2 = minBeacon.getLng() + (min * Math.cos(slope.doubleValue()));

            Log.w("NavUtils: cords " , x2 + " --- " + y2);

            return new BeaconCls(
                    "mobile",
                    x2,
                    y2
            );

        } catch (Exception e){
            e.printStackTrace();
        }

        return new BeaconCls("", 0f, 0f);

    }
}
