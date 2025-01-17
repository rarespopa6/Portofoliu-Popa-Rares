package org.bank.repository;

import org.bank.model.Identifiable;

import java.io.IOException;
import java.util.List;

/**
 * Interface for a generic repository that provides CRUD (Create, Read, Update, Delete) operations.
 *
 * @param <T> The type of objects to be managed by this repository - Identifiable.
 */
public interface IRepository<T extends Identifiable> {

    /**
     * Creates a new record in the repository for the specified object.
     *
     * @param obj the object to create in the repository.
     * @return the unique identifier (ID) of the created object.
     */
    int create(T obj);

    /**
     * Reads an object from the repository by its unique identifier.
     *
     * @param id the unique identifier of the object to read.
     * @return the object with the specified ID, or null if not found.
     */
    T read(int id);

    /**
     * Updates the specified object in the repository.
     *
     * @param obj the object to update in the repository.
     */
    void update(T obj);

    /**
     * Deletes an object from the repository by its unique identifier.
     *
     * @param id the unique identifier of the object to delete.
     */
    void delete(int id) throws IOException;

    /**
     * Get all objects in the repository.
     */
    List<T> findAll();
}
