package org.bank.repository;

import org.bank.model.Identifiable;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

/**
 * In-memory implementation of the {@link IRepository} interface, storing data in a HashMap.
 * This class provides basic CRUD (Create, Read, Update, Delete) operations, with support for objects that
 * implement the {@link Identifiable} interface.
 *
 * @param <T> The type of objects to be managed by this repository - Identifiable.
 */
public class InMemoryRepository<T extends Identifiable> implements IRepository<T> {
    private final Map<Integer, T> data = new HashMap<>();
    private int currentId = 0;

    /**
     * Creates a new entry in the repository and assigns it a unique ID.
     * If the object implements {@link Identifiable}, its ID is set to the generated ID.
     *
     * @param obj the object to be added to the repository.
     * @return the unique ID assigned to the created object.
     */
    @Override
    public int create(T obj) {
        int newId = ++currentId;
        obj.setId(newId);

        data.put(newId, obj);
        return newId;
    }

    /**
     * Reads an object from the repository based on its ID.
     *
     * @param id the unique identifier of the object to read.
     * @return the object with the specified ID, or {@code null} if not found.
     */
    @Override
    public T read(int id) {
        return data.get(id);
    }

    /**
     * Updates an existing object in the repository. The object must already exist in the repository.
     * If the object is not found, a {@link RuntimeException} is thrown.
     *
     * @param obj the object to update.
     * @throws RuntimeException if the object is not present in the repository.
     */
    @Override
    public void update(T obj) throws RuntimeException {
        int id = getIdFromObj(obj);
        if (data.containsKey(id)) {
            data.put(id, obj);
        } else {
            throw new RuntimeException("Invalid update");
        }
    }

    /**
     * Deletes an object from the repository by its unique ID.
     *
     * @param id the unique identifier of the object to delete.
     */
    @Override
    public void delete(int id) {
        data.remove(id);
    }

    /**
     * Retrieves a list of all objects currently stored in the repository.
     *
     * @return a list of all objects in the repository.
     */
    public List<T> findAll() {
        return new ArrayList<>(data.values());
    }

    /**
     * Helper method to extract the ID from an object using reflection.
     * Assumes the object has a {@code getId} method that returns an Integer.
     *
     * @param obj the object from which to extract the ID.
     * @return the ID of the object.
     * @throws RuntimeException if the ID cannot be extracted.
     */
    private int getIdFromObj(T obj) {
        try {
            return (Integer) obj.getClass().getMethod("getId").invoke(obj);
        } catch (Exception e) {
            throw new RuntimeException("Error extracting ID from object", e);
        }
    }
}
