package org.bank.model;

/**
 * Interface for objects that can be identified by a unique ID.
 * Classes implementing this interface must provide methods to get and set the ID.
 */
public interface Identifiable {

    /**
     * Retrieves the unique identifier of the object.
     *
     * @return the unique ID of the object
     */
    int getId();

    /**
     * Sets the unique identifier for the object.
     *
     * @param id the unique ID to set for the object
     */
    void setId(int id);
}
