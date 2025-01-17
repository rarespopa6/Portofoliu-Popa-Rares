package org.bank.model.mapper;

import org.bank.model.Customer;
import org.bank.model.Employee;
import org.bank.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Mapper class for mapping a {@link ResultSet} to a {@link User} object.
 * This class handles the mapping logic for both {@link Customer} and {@link Employee} types.
 */
public class UserMapper implements Mapper<User> {

    /**
     * Maps a {@link ResultSet} to a {@link User} object. Depending on the user type in the {@link ResultSet},
     * this method will instantiate either a {@link Customer} or an {@link Employee}.
     *
     * @param resultSet The {@link ResultSet} containing the user data from the database.
     * @return A {@link User} object, either {@link Customer} or {@link Employee}, populated with data from the {@link ResultSet}.
     * @throws SQLException If there is an error accessing the data in the {@link ResultSet}.
     * @throws IllegalArgumentException If an unknown user type is encountered in the {@link ResultSet}.
     */
    @Override
    public User map(ResultSet resultSet) throws SQLException {
        String userType = resultSet.getString("type");
        User user;

        if ("CUSTOMER".equalsIgnoreCase(userType)) {
            user = new Customer(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("password")
            );
            ((Customer) user).setAccountList(new ArrayList<>());
            ((Customer) user).setLoanList(new ArrayList<>());
        } else if ("EMPLOYEE".equalsIgnoreCase(userType)) {
            user = new Employee(
                    resultSet.getInt("id"),
                    resultSet.getString("first_name"),
                    resultSet.getString("last_name"),
                    resultSet.getString("email"),
                    resultSet.getString("phone_number"),
                    resultSet.getString("password"),
                    resultSet.getInt("salary"),
                    resultSet.getString("role")
            );
        } else {
            throw new IllegalArgumentException("Unknown user type: " + userType);
        }

        return user;
    }
}
