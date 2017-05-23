package com.example.dao;

import com.example.domain.Nutrition;
import com.example.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import sun.tools.java.Type;

import java.sql.*;
import java.util.List;

/**
 * Created by bsheen on 5/19/17.
 */
@Repository
public class ProductDaoImpl implements ProductDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final String ADD_PRODUCT_SQL = "INSERT INTO product (name, brand) VALUES (?,?)";

    @Override
    public int add(Product product) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (product != null) {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(ADD_PRODUCT_SQL, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, product.getName());
                    if (product.getBrand() == null) {
                        ps.setNull(2, Types.VARCHAR);
                    } else {
                        ps.setString(2, product.getBrand());
                    }
                    return ps;
                }
            }, keyHolder);
        }
        int id = (int) keyHolder.getKey().longValue();
        product.setId(id);
        return id;
    }

    @Override
    public List<Product> findAll() {
        List<Product> productList = jdbcTemplate.query("SELECT * FROM product", new ProductMapper());
        return productList;
    }

    @Override
    public Product find(long product_id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM product WHERE id = ?", new ProductMapper(), product_id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static final String ADD_PRODUCT_TO_NUTRITION_SQL = "UPDATE nutrition SET nutrition.product_id = ? WHERE nutrition.id = ?";

    @Override
    public void addProductToNutrition(Product product, Nutrition nutrition) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(ADD_PRODUCT_TO_NUTRITION_SQL);
                ps.setLong(1, product.getId());
                ps.setLong(2, nutrition.getId());
                return ps;
            }
        });
    }

    @Override
    @Transactional
    public void addProductToNutritions(List<Nutrition> nutritions, Product product) {
        for (Nutrition nutrition : nutritions) {
            addProductToNutrition(product, nutrition);
        }
    }

    private static final String REMOVE_PRODUCT_FROM_NUTRITION_SQL = "UPDATE nutrition SET nutrition.product_id = null WHERE nutrition.id = ?";

    @Override
    @Transactional
    public void removeProductFromNutrition(Nutrition nutrition) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(REMOVE_PRODUCT_FROM_NUTRITION_SQL);
                ps.setLong(1, nutrition.getId());
                return ps;
            }
        });
    }

    @Override
    @Transactional
    public void removeProductFromNutritions(List<Nutrition> nutritions) {
        for(Nutrition nutrition:nutritions){
            removeProductFromNutrition(nutrition);
        }
    }

    private static final String UPDATE_PRODUCT_SQL = "UPDATE product SET product.name = ?, product.brand = ? WHERE product.id = ?";

    @Override
    public void updateProduct(Product product) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(UPDATE_PRODUCT_SQL);
                ps.setString(1,product.getName());
                if(product.getBrand()==null){
                    ps.setNull(2, Types.VARCHAR);
                } else {
                    ps.setString(2,product.getBrand());
                }
                ps.setLong(3,product.getId());
                return ps;
            }
        });
    }

    private static final String FIND_NUTRITIONS_WITH_PRODUCT_SQL = "SELECT * FROM nutrition WHERE product_id = ?";

    @Override
    public List<Nutrition> findNutritionsWithProduct(long product_id) {
        List<Nutrition> nutritionList = jdbcTemplate.query(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                 PreparedStatement ps = connection.prepareStatement(FIND_NUTRITIONS_WITH_PRODUCT_SQL);
                 ps.setLong(1,product_id);
                 return ps;
            }
        },new NutritionDaoImpl.NutritionMapper());
        return nutritionList;
    }

    private static final String DELETE_PRODUCT_SQL = "DELETE FROM product WHERE id=?";

    @Override
    @Transactional
    public void deleteProduct(long product_id) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(DELETE_PRODUCT_SQL);
                ps.setLong(1,product_id);
                return ps;
            }
        });
    }

    public static class ProductMapper implements RowMapper<Product> {
        @Override
        public Product mapRow(ResultSet resultSet, int i) throws SQLException {
            Product product = new Product();
            product.setId(resultSet.getLong("id"));
            product.setName(resultSet.getString("name"));
            if (resultSet.getString("brand") != null) {
                product.setBrand(resultSet.getString("brand"));
            }
            return product;
        }
    }
}
