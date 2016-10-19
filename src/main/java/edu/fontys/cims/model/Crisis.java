/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.fontys.cims.model;

/**
 *
 * @author Jip
 */
public class Crisis {
    private int id;
    private Alert alert;
    private int oid;
    private String title;
    private String status;
    private String description;
    private int priority;
    private int reach;
    private String thumbnail;
    private String timestamp;
    
    /**
     * Constructor for crisis before it is send to the database.
     * @param alert Reference to the alert.
     * @param oid database reference to the operator.
     * @param status State of the crisis.
     * @param description description of the crisis.
     * @param priority Priority of the crisis.
     * @param reach Area of effect.
     * @param title Title of the crisis.
     */
    public Crisis( Alert alert, int oid, String status, String description, int priority, int reach, String title ) {
        this.alert = alert;
        this.oid = oid;
        this.status = status;
        this.description = description;
        this.priority = priority;
        this.reach = reach;
        this.title = title;
    }

    public Crisis(int id, Alert alert, int oid, String status, int priority, int reach, String title, String thumbnail, String timestamp) {
        this.id = id;
        this.alert = alert;
        this.oid = oid;
        this.status = status;
        this.priority = priority;
        this.reach = reach;
        this.title = title;
        this.thumbnail = thumbnail;
        this.timestamp = timestamp;
    }
    
    public Crisis(int id, int oid, String status, int priority, int reach, String title, String thumbnail, String timestamp) {
        this.id = id;
        this.oid = oid;
        this.status = status;
        this.priority = priority;
        this.reach = reach;
        this.title = title;
        this.thumbnail = thumbnail;
        this.timestamp = timestamp;
    }

    /**
     * Get the value of title
     *
     * @return the value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Set the value of title
     *
     * @param title new value of title
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    /**
     * Get the value of description
     *
     * @return the value of description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the value of description
     *
     * @param description new value of description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Get the value of reach
     *
     * @return the value of reach
     */
    public int getReach() {
        return reach;
    }

    /**
     * Set the value of reach
     *
     * @param reach new value of reach
     */
    public void setReach(int reach) {
        this.reach = reach;
    }

    /**
     * Get the value of priority
     *
     * @return the value of priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Set the value of priority
     *
     * @param priority new value of priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Get the value of status
     *
     * @return the value of status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Set the value of status
     *
     * @param status new value of status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Get the value of oid
     *
     * @return the value of oid
     */
    public int getOid() {
        return oid;
    }

    /**
     * Set the value of oid
     *
     * @param oid new value of oid
     */
    public void setOid(int oid) {
        this.oid = oid;
    }

    /**
     * Get the alert object
     *
     * @return the object of alert
     */
    public Alert getAlert() {
        return alert;
    }
    
    public void setAlert( Alert alert ) {
        this.alert = alert;
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
     * Set the value of id
     *
     * @param id new value of id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * get the thumbnail of the crisis
     *
     * @return thumbnail link
     */
    public String getThumbnail() {
        return thumbnail;
    }

    /**
     * get the timestamp of the crisis
     *
     * @return crisis time
     */
    public String getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return title + "{" + "id=" + id + ", aid=" + alert.toString() + ", oid=" + oid + ", status=" + status + ", priority=" + priority + ", reach=" + reach + '}';
    }
}

