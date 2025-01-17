package org.bank.model.mapper;

import org.bank.model.Account;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

/**
 * Utility class containing helper methods for mapping database attributes to Java objects.
 * This class is intended for internal use in the data mapping process.
 */
public class MapperUtils {

    /**
     * Maps the common attributes of an {@link Account} from the provided {@link ResultSet}.
     * This method is intended to be used by specific account mappers to populate shared account fields.
     *
     * @param resultSet The {@link ResultSet} containing the account data from the database.
     * @param account The {@link Account} object to be populated with the data from the {@link ResultSet}.
     * @throws SQLException If there is an error accessing the data in the {@link ResultSet}.
     */
    public static void mapAccountAttributes(ResultSet resultSet, Account account) throws SQLException {
        account.setId(resultSet.getInt("id"));
        account.setBalance(resultSet.getDouble("balance"));
        account.setCreationTime(resultSet.getTimestamp("creation_time").toLocalDateTime());
    }
}
