package model;

/**
 * Model class for Release
 * Mapped to JSON nested object 'release' for each song in JSON file
 */
public class Release {

    private int id;
    private String name;

/**
 * Constructor for Song class
 * Parameters for each entry in JSON file under nested object 'song'
 */
    public Release(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
