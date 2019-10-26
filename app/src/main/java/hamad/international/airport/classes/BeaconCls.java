package hamad.international.airport.classes;

public class BeaconCls {

    private String id1;
    private double lat;
    private double lng;

    public BeaconCls(String id1, double lat, double lng) {
        this.id1 = id1;
        this.lat = lat;
        this.lng = lng;
    }

    public String getId1() {
        return id1;
    }

    public void setId1(String id1) {
        this.id1 = id1;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
}
