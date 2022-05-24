package com.company.dao;

import com.company.tableObjects.Event;
import com.company.tableObjects.Installment;

import java.sql.*;
import java.util.ArrayList;
import java.util.Optional;

public class InstallmentDao implements Dao<Installment>{
    private final Connection connection;

    public InstallmentDao(Connection connection) {
        this.connection = connection;
    }

    private Installment getInstallment(ResultSet rs) throws SQLException {
        return new Installment(rs.getInt("installment_id"),rs.getInt("event_id"),rs.getInt("installment_number"),rs.getDate("deadline"), rs.getDouble("installment_amount"));
    }

    @Override
    public Optional<Installment> get(int id) {
        Installment installment = null;

        String sql = "SELECT * FROM \"installment\" WHERE \"installment_id\"=?;";

        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1, id);

            ResultSet rs = statement.executeQuery();

            while (rs.next()) {
                installment = getInstallment(rs);
            }

//            statement.close();
//            rs.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return Optional.ofNullable(installment);
    }

    @Override
    public ArrayList<Installment> getAll() {
        ArrayList<Installment> installments = new ArrayList<>();

        String sql = "SELECT * FROM \"installment\";";
        try(PreparedStatement statement= connection.prepareStatement(sql)){
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                installments.add(getInstallment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return installments;
    }

    @Override
    public void save(Installment installment) {
        String sql = "INSERT INTO \"installment\" (\"event_id\", \"installment_number\", \"deadline\", \"installment_amount\") VALUES  (?, ?, ?, ?);";

        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1, installment.getEventId());
            statement.setInt(2, installment.getNumber());
            statement.setDate(3, installment.getDeadline());
            statement.setDouble(4,installment.getAmount());
            statement.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void update(Installment installment, String[] params) {
        String sql = "UPDATE \"installment\" SET \"event_id\"=?, \"installment_number\"=?, \"deadline\"=? ,\"installment_amount\"=? WHERE \"installment_id\"=?;";

        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1, installment.getEventId());
            statement.setInt(2, installment.getNumber());
            statement.setDate(3, installment.getDeadline());
            statement.setDouble(4, installment.getAmount());
            statement.setInt(5, installment.getId());
            statement.executeUpdate();

            //connection.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Installment installment) {

        String sql = "DELETE FROM \"installment\" WHERE \"installment_id\"=?;";

        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1, installment.getId());
            statement.executeUpdate();

            // connection.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Installment> getEventInstallments(int id) {
        ArrayList<Installment> installments = new ArrayList<>();

        String sql = "SELECT * FROM \"installment\" WHERE \"event_id\"=?;";
        try(PreparedStatement statement= connection.prepareStatement(sql)){
            statement.setInt(1,id);
            ResultSet rs = statement.executeQuery();

            while(rs.next()){
                installments.add(getInstallment(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return installments;
    }
}
