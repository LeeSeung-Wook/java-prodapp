package server;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    // 1️⃣ insert
    public void insert(String name, int price, int qty) {
        String sql = "INSERT INTO productdb(name, price, qty) VALUES (?, ?, ?)";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setString(1, name);
            pstmt.setInt(2, price);
            pstmt.setInt(3, qty);
            pstmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 2️⃣ deleteById
    public void deleteById(int id) {
        String sql = "DELETE FROM productdb WHERE id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 3️⃣ findById
    public Product findById(int id) {
        String sql = "SELECT id, name, price, qty FROM productdb WHERE id = ?";

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)
        ) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getInt("qty")
                );
            }
            return null;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 4️⃣ findAll
    public List<Product> findAll() {
        String sql = "SELECT id, name, price, qty FROM productdb";
        List<Product> list = new ArrayList<>();

        try (
                Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()
        ) {
            while (rs.next()) {
                list.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("price"),
                        rs.getInt("qty")
                ));
            }
            return list;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
