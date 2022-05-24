package com.company.dao;

import com.company.tableObjects.Event;
import com.company.tableObjects.Installment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class EventDao implements Dao<Event>{
    private final Connection connection;

    public EventDao(Connection connection) {
        this.connection = connection;
    }

    private Event getEvent(ResultSet rs) throws SQLException {
        return new Event(rs.getInt("event_id"),rs.getString("name"),rs.getString("place"),rs.getDate("date"));
    }

    @Override
    public Optional<Event> get(int id) {
        Event event = null;

        String sql = "SELECT * FROM \"event\" WHERE \"event_id\"=?;";

        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                event = getEvent(rs);
                System.out.println(event.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(event);
    }


    @Override
    public ArrayList<Event> getAll() {
        ArrayList<Event> events = new ArrayList<>();

        String sql = "SELECT * FROM \"event\";";
        try(PreparedStatement statement= connection.prepareStatement(sql)){
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                events.add(getEvent(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }


    @Override
    public void save(Event event) {
        String sql = "INSERT INTO \"event\" (\"name\", \"place\", \"date\") VALUES  (?, ?, ?);";

        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, event.getName());
            statement.setString(2, event.getPlace());
            statement.setDate(3, event.getDate());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Event event, String[] params) {
        String sql = "UPDATE \"event\" SET \"name\"=?, \"place\"=?, \"date\"=? WHERE \"event_id\"=?;";

        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, event.getName());
            statement.setString(2, event.getPlace());
            statement.setDate(3, event.getDate());
            statement.setInt(4,event.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Event event) {

        String sql = "DELETE FROM \"event\" WHERE \"event_id\"=?;";

        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1, event.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
