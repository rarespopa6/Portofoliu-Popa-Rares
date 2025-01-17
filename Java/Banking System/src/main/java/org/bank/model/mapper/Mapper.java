package org.bank.model.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The Mapper interface defines the contract for mapping a {@link ResultSet} from a database query
 * to a specific object of type {@link T}.
 *
 * @param <T> The type of object to which the result set will be mapped.
 */
public interface Mapper<T> {

    /**
     * Maps the given {@link ResultSet} to an object of type {@link T}.
     *
     * @param resultSet The {@link ResultSet} to be mapped to the object.
     * @return The object of type {@link T} populated with data from the result set.
     * @throws SQLException If there is an error accessing the result set.
     */
    T map(ResultSet resultSet) throws SQLException;
}
