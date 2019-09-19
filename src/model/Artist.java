package model;

/**
 * Model class for Artist
 * Mapped to JSON nested object 'artist' for each song in JSON file
 */
public class Artist {

    private double terms_freq;
    private String terms;
    private String name;
    private double familiarity;
    private double longitude;
    private String id;
    private String location;
    private double latitude;
    private String similar;
    private double hotttnesss;

    /**
     * Constructor for Artist class
     * Parameters for each entry in JSON file under nested object 'artist'
     * @param terms_freq
     * @param terms
     * @param name
     * @param familiarity
     * @param longitude
     * @param id
     * @param location
     * @param latitude
     * @param similar
     * @param hotttnesss
     */
    public Artist(double terms_freq, String terms, String name, double familiarity, double longitude, String id, String location, double latitude, String similar, double hotttnesss) {
        this.terms_freq = terms_freq;
        this.terms = terms;
        this.name = name;
        this.familiarity = familiarity;
        this.longitude = longitude;
        this.id = id;
        this.location = location;
        this.latitude = latitude;
        this.similar = similar;
        this.hotttnesss = hotttnesss;
    }

    public double getTerms_freq() {
        return terms_freq;
    }

    public String getTerms() {
        return terms;
    }

    public String getName() {
        return name;
    }

    public double getFamiliarity() {
        return familiarity;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getSimilar() {
        return similar;
    }

    public double getHotttnesss() {
        return hotttnesss;
    }
}
