package com.pasteleria.crudApp.Repository.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pasteleria.crudApp.Exception.ProductServiceException;
import com.pasteleria.crudApp.Model.Product;
import com.pasteleria.crudApp.Repository.ProductRepository;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    @Autowired
    private DataSource dataSource;

    @Override
    public void addProduct(Product product) {
        String query = "INSERT INTO Product(name, productTypeId, description, price, stock) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getProductTypeId());
            pstmt.setString(3, product.getDescription());
            pstmt.setInt(4, product.getPrice());
            pstmt.setInt(5, product.getStock());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateProduct(Product product) {
        String query = "UPDATE Product SET name = ?, ProductTypeID = ?, description = ?, price = ?, stock = ? WHERE productID = ?";
        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, product.getName());
            pstmt.setInt(2, product.getProductTypeId());
            pstmt.setString(3, product.getDescription());
            pstmt.setInt(4, product.getPrice());
            pstmt.setInt(5, product.getStock());
            pstmt.setInt(6, product.getProductId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteProduct(int productId) {
        String query = "DELETE FROM Product WHERE productID = ?";
        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> getAllProducts() {
        List<Product> result = new LinkedList<>();
        try (Connection con = dataSource.getConnection()) {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM Product");

            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getInt("productID"));
                product.setName(rs.getString("name"));
                product.setProductTypeId(rs.getInt("ProductTypeID"));
                product.setPrice(rs.getInt("price"));
                product.setStock(rs.getInt("stock"));
                product.setDescription(rs.getString("description"));
                result.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public Product getProductById(int productId) {
        Product product = null;
        String query = "SELECT * FROM Product WHERE productId = ?";
        try (Connection con = dataSource.getConnection();
                PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, productId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    product = new Product();
                    product.setProductId(rs.getInt("productID"));
                    product.setName(rs.getString("name"));
                    product.setProductTypeId(rs.getInt("ProductTypeID"));
                    product.setPrice(rs.getInt("price"));
                    product.setStock(rs.getInt("stock"));
                    product.setDescription(rs.getString("description"));
                }
            }
        } catch (SQLException e) {
            throw new ProductServiceException("Error retrieving product by ID: " + e.getMessage());
        }
        return product;
    }

}
