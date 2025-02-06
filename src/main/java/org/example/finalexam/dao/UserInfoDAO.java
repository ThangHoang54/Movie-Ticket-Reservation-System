package org.example.finalexam.dao;

import org.example.finalexam.model.User;

import java.sql.SQLException;

/**
 * @author Hoang Minh Thang - s3999925
 */

public interface UserInfoDAO {
    User getUserByFullNameAndEmail(String fullName, String email) throws SQLException;
}
