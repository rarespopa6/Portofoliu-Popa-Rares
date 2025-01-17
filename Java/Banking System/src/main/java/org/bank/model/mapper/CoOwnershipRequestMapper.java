package org.bank.model.mapper;

import org.bank.model.CheckingAccount;
import org.bank.model.CoOwnershipRequest;
import org.bank.model.Customer;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CoOwnershipRequestMapper implements Mapper<CoOwnershipRequest> {

    @Override
    public CoOwnershipRequest map(ResultSet rs) throws SQLException {
        CoOwnershipRequest request = new CoOwnershipRequest(
                new CheckingAccount(rs.getInt("account_id")),
                new Customer(rs.getInt("requester_id")),
                new Customer(rs.getInt("owner_id"))
        );

        request.setId(rs.getInt("id"));
        request.setApproved(rs.getBoolean("approved"));

        return request;
    }
}
