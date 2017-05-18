package com.example.dao;

import com.example.Application;
import com.example.common.FoodGroup;
import com.example.domain.Nutrition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by bsheen on 5/8/17.
 */

@Repository
public class NutritionDaoImpl implements NutritionDao {

    private static final Logger log = LoggerFactory.getLogger(NutritionDaoImpl.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public Nutrition find(long id) {
        try {
            return jdbcTemplate.queryForObject("SELECT * FROM nutrition WHERE nutrition.id = ?", new NutritionMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static final String UPDATE_NUTRITION_SQL = "UPDATE nutrition SET nutrition.product = ?, nutrition.calories = ?, nutrition.carbs = ?, nutrition.foodgroup = ?, nutrition.favorite = ? WHERE nutrition.id = ?";

    @Override
    public void update(Nutrition nutrition) {
        jdbcTemplate.update(new PreparedStatementCreator() {
            @Override
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(UPDATE_NUTRITION_SQL);
                ps.setString(1, nutrition.getProduct());
                ps.setInt(2, nutrition.getCalories());
                ps.setInt(3, nutrition.getCarbs());
                if (nutrition.getGroup() == null) {
                    ps.setNull(4, Types.VARCHAR);
                } else {
                    ps.setString(4, nutrition.getGroup().name());
                }
                if (nutrition.getFavorite() == null) {
                    ps.setNull(5, Types.BOOLEAN);
                } else {
                    ps.setBoolean(5, nutrition.getFavorite());
                }
                ps.setLong(6, nutrition.getId());
                return ps;
            }
        });
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM nutrition WHERE nutrition.id = ?", id);
    }

    @Override
    @Transactional
    public void add(List<Nutrition> nutritions) {
        for (Nutrition nutrition : nutritions) {
            add(nutrition);
        }
    }

    private static final String ADD_NUTRITION_SQL = "INSERT INTO nutrition (product, calories, carbs, foodgroup, favorite) VALUES (?,?,?,?,?)";

    @Override
    public int add(Nutrition nutrition) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (nutrition != null) {
            jdbcTemplate.update(new PreparedStatementCreator() {
                @Override
                public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                    PreparedStatement ps = connection.prepareStatement(ADD_NUTRITION_SQL, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, nutrition.getProduct());
                    ps.setInt(2, nutrition.getCalories());
                    ps.setInt(3, nutrition.getCarbs());
                    if (nutrition.getGroup() == null) {
                        ps.setNull(4, Types.VARCHAR);
                    } else {
                        ps.setString(4, nutrition.getGroup().name());
                    }
                    if (nutrition.getFavorite() == null) {
                        ps.setNull(5, Types.BOOLEAN);
                    } else {
                        ps.setBoolean(5, nutrition.getFavorite());
                    }
                    return ps;
                }
            }, keyHolder);
        }
        int id = (int) keyHolder.getKey().longValue();
        nutrition.setId(id);
        return id;
    }

    @Override
    public List<Nutrition> findAll() {
        List<Nutrition> nutritions = jdbcTemplate.query("SELECT * FROM nutrition", new NutritionMapper());
        return nutritions;
    }

    public static class NutritionMapper implements RowMapper<Nutrition> {
        @Override
        public Nutrition mapRow(ResultSet resultSet, int i) throws SQLException {
            Nutrition nutrition = new Nutrition();
            nutrition.setProduct(resultSet.getString("product"));
            nutrition.setCarbs(resultSet.getInt("carbs"));
            nutrition.setCalories(resultSet.getInt("calories"));
            nutrition.setId(resultSet.getLong("id"));
            if (resultSet.getString("foodgroup") != null) {
                nutrition.setGroup(FoodGroup.valueOf(resultSet.getString("foodgroup")));
            }
            if (resultSet.getObject("favorite") != null) {
                nutrition.setFavorite(resultSet.getBoolean("favorite"));
            }
            return nutrition;
        }
    }

}
