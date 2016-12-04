/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fontys.cims.model;

/**
 * @author Jip
 */
public class Alert {
    private Location location;
    private int id;
    private String ipaddr;
    private boolean processed;
    private String description;
    private String timestamp;

    /**
     * Database constructor.
     *
     * @param id id in the database.
     * @param ipaddr IP address of the person who made the alert.
     * @param processed 0 = not processed, 1 = processed in integer.
     * @param description The provided description of the situation.
     * @param timestamp the time when the alert was made.
     */
    public Alert(int id, String ipaddr, int processed, String description, String timestamp) {
        if (processed < 0 || processed > 1) {
            throw new IllegalArgumentException("Parameter int processed cant be cast to boolean.");
        }
        this.id = id;
        this.ipaddr = ipaddr;
        //if processed is higher then 0 its true else false
        this.processed = (processed > 0);
        this.description = description;
        this.timestamp = timestamp;
    }

    /**
     * Default constructor.
     *
     * @param loc Location object from the location of the alert.
     * @param id id in the database.
     * @param ipaddr IP address of the person who made the alert.
     * @param processed 0 = not processed, 1 = processed in integer.
     * @param description The provided description of the situation.
     * @param timestamp the time when the alert was made.
     */
    public Alert(int id, Location loc, String ipaddr, int processed, String description, String timestamp) {
        if (processed < 0 || processed > 1) {
            throw new IllegalArgumentException("Parameter int processed cant be cast to boolean.");
        }

        this.id = id;
        this.location = loc;
        this.ipaddr = ipaddr;
        //if processed is higher then 0 its true else false
        this.processed = (processed > 0);
        this.description = description;
        this.timestamp = timestamp;
    }

    /**
     * Get the value of processed
     *
     * @return the value of processed
     */
    public boolean getProcessed() {
        return processed;
    }

    /**
     * set processed to true
     */
    public void processAlert() {
        this.processed = true;
    }

    /**
     * Get the value of ipaddr
     *
     * @return the value of ipaddr
     */
    public String getIpaddr() {
        return ipaddr;
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
     * Get the value of location
     *
     * @return the value of location
     */
    public Location getLocation() {
        return location;
    }

    /**
     * set location
     * @param location  new location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * get the description a person has provided.
     *
     * @return alert description
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * get the timestamp of the alert
     *
     * @return alert time
     */
    public String getTimestamp() {
        return this.timestamp;
    }
    
    public String listString(){
        return location.getCity() + ", " + timestamp;
    }

    @Override
    public String toString() {
        return "Alert{" + "location=" + location + ", id=" + id + ", ipaddr=" + ipaddr + ", processed=" + processed + '}';
    }
}

