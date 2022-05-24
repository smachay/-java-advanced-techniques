package com.company.dao;

import com.company.tableObjects.Person;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PersonDao implements Dao<Person> {
    private final Connection connection;

    public PersonDao(Connection connection) {
        this.connection = connection;
    }

    public Person getPerson(ResultSet rs) throws SQLException {
        return new Person(rs.getInt("person_id"),rs.getString("name"),rs.getString("surname"));
    }

    @Override
    public Optional<Person> get(int id) {
        Person person = null;
        String sql = "SELECT * FROM \"person\" WHERE \"person_id\"=?;";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                person = getPerson(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(person);
    }

    @Override
    public ArrayList<Person> getAll() {
        ArrayList<Person> persons = new ArrayList<>();
        String sql = "SELECT * FROM \"person\";";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                persons.add(getPerson(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return persons;
    }

    @Override
    public void save(Person person) {
        String sql = "INSERT INTO \"person\" (\"name\", \"surname\") VALUES  (?, ?);";

        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, person.getName());
            statement.setString(2, person.getSurname());
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Person person, String[] params) {
        String sql = "UPDATE \"person\" SET \"name\"=?, \"surname\"=? WHERE \"person_id\"=?;";

        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setString(1, params[0]);
            statement.setString(2, params[1]);
            statement.setInt(3,person.getId());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Person person) {
        String sql = "DELETE FROM \"person\" WHERE \"person_id\"=? ;";

        try(PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1,person.getId());
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
