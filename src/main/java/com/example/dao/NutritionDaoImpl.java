package com.example.dao;

import com.example.Application;
import com.example.domain.Nutrition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
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

    @Override
    public void update(Nutrition nutrition) {
        jdbcTemplate.update("UPDATE nutrition SET nutrition.product = ?, nutrition.calories = ?, nutrition.carbs = ? WHERE nutrition.id = ?", nutrition.getProduct(), nutrition.getCalories(), nutrition.getCarbs(), nutrition.getId());
    }

    @Override
    public void delete(long id) {
        jdbcTemplate.update("DELETE FROM nutrition WHERE nutrition.id = ?", id);
    }

    @Override
    public void add(List<Nutrition> nutritions) {
        for (Nutrition nutrition : nutritions) {
            jdbcTemplate.update("INSERT INTO nutrition (product, calories, carbs) VALUES (?,?,?)", nutrition.getProduct(), nutrition.getCalories(), nutrition.getCarbs());
        }

    }

    @Override
    public void add(Nutrition nutrition) {
        if (nutrition != null) {
            jdbcTemplate.update("INSERT INTO nutrition (product, calories, carbs) VALUES (?,?,?)", nutrition.getProduct(), nutrition.getCalories(), nutrition.getCarbs());
        }
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
            return nutrition;
        }
    }

}
