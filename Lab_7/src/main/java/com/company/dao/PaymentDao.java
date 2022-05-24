package com.company.dao;

import com.company.tableObjects.Payment;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Optional;

public class PaymentDao implements Dao<Payment>{
    private final Connection connection;

    public PaymentDao(Connection connection) {
        this.connection = connection;
    }

    public Payment getPerson(ResultSet rs) throws SQLException {
        return new Payment(rs.getInt("payment_id"),rs.getDate("payment_date"), rs.getDouble("payment_amount"), rs.getInt("person_id"), rs.getInt("event_id"),rs.getInt("installment_number"));
    }

    @Override
    public Optional<Payment> get(int id) {
        Payment payment = null;
        String sql = "SELECT * FROM \"person\" WHERE \"person_id\"=? ;";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                payment = getPerson(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.ofNullable(payment);
    }

    @Override
    public ArrayList<Payment> getAll() {
        ArrayList<Payment> payments = new ArrayList<>();

        String sql = "SELECT * FROM \"payment\";";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                payments.add(getPerson(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return payments;
    }

    @Override
    public void save(Payment payment) {
        String sql = "INSERT INTO \"payment\" (\"payment_date\", \"payment_amount\", \"person_id\", \"event_id\", \"installment_number\") VALUES  ((SELECT CURRENT_DATE), ?, ?, ?, ?);";

        try (PreparedStatement statement = connection.prepareStatement(sql);){
            statement.setDouble(1, payment.getAmount());
            statement.setInt(2, payment.getPersonId());
            statement.setInt(3, payment.getEventId());
            statement.setInt(4, payment.getInstallmentNumber());
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Payment payment, String[] params) {
        String sql = "UPDATE \"payment\" SET \"payment_date\"=?, \"payment_amount\"=?, \"person_id\"=?, \"event_id\"=?, \"installment_number\"=? where \"payment_id\" = ?;";

        try (PreparedStatement statement = connection.prepareStatement(sql);) {
            statement.setString(1, params[0]);
            statement.setString(2, params[1]);
            statement.setString(3, params[2]);
            statement.setString(4, params[3]);
            statement.setString(5, params[4]);
            statement.setInt(6, payment.getId());
            statement.executeUpdate();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Payment payment) {
        String sql = "DELETE FROM \"payment\" WHERE \"payment_id\"=?;";

        try (PreparedStatement statement = connection.prepareStatement(sql); ){
            statement.setInt(1, payment.getEventId());
            statement.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
