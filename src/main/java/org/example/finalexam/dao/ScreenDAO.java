package org.example.finalexam.dao;

import org.example.finalexam.model.Screen;

import java.sql.SQLException;
import java.util.List;

/**
 * @author Hoang Minh Thang - s3999925
 */

public interface ScreenDAO {
    void addScreen(Screen screen) throws SQLException;
    Screen getScreenById(int id) throws SQLException;
    void updateScreen(Screen screen) throws SQLException;
    void deleteScreen(int id) throws SQLException;
    List<Screen> getAllScreens() throws SQLException;
    int getScreenIDByScreenName(String screenName) throws SQLException;

}
