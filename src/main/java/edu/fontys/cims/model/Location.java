/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fontys.cims.model;

/**
 *
 * @author juleb
 */
public class Location {
    private int id;
    private String latitude;
    private String longitude;
    private String country;
    private String countryCode;
    private String city;
    private String zipcode;
    private String streetName;
    private int streetNumber;

    /**
     * Default constructor.
     *
     * @param id id in the database.
     * @param latitude latitude of the location.
     * @param longitude longitude of the location.
     * @param country country of the given location.
     * @param countryCode the code of the given country.
     * @param city the city where the alert is happening.
     * @param zipcode zipcode of the given location.
     * @param streetName name of the street where the alert is happening.
     * @param streetNumber the street number of the given street.
     */
    public Location(int id, String latitude, String longitude, String country, String countryCode, String city, String zipcode, String streetName, int streetNumber) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.countryCode = countryCode;
        this.city = city;
        this.zipcode = zipcode;
        this.streetName = streetName;
        this.streetNumber = streetNumber;
    }

    /**
     * Get the value of id
     *
     * @return the value of id
     */
    public int getId() {
        return id;
    }

    /**
     * get the latitude and longitude of the alert
     *
     * @return the lat[0] and longitude[1]
     */
    public String[] getPosition() {
        return new String[]{latitude, longitude};
    }

    /**
     * Get the country of the alert
     *
     * @return the country as String
     */
    public String getCountry() {
        return country;
    }

    /**
     * Get the country code of the alert
     *
     * @return the country code as integer
     */
    public String getCountryCode() {
        return countryCode;
    }

    /**
     * Get the city of the alert
     *
     * @return the city as String
     */
    public String getCity() {
        return city;
    }

    /**
     * Get the zipcode of the alert
     *
     * @return the zipcode as String
     */
    public String getZipcode() {
        return zipcode;
    }

    /**
     * Get the street name of the alert
     *
     * @return the street name as string
     */
    public String getStreetName() {
        return streetName;
    }

    /**
     * Get the street number of the alert
     *
     * @return the street number as integer
     */
    public int getStreetNumber() {
        return streetNumber;
    }

    @Override
    public String toString() {
        return "Land: " + countryCode + ", stad: " + city + ", postcode: " + zipcode + ", straat: " + streetName + ", huisnr: " + streetNumber;
    }
}
