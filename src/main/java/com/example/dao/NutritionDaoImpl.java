package com.example.dao;

import com.example.Application;
import com.example.domain.Nutrition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

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
    public void add(Nutrition nutrition) {
        if(nutrition != null){
            jdbcTemplate.execute(String.format("INSERT INTO nutrition (product, calories, carbs) VALUES (%s, %s, %s)", nutrition.getProduct(),nutrition.getCalories(),nutrition.getCarbs()));
        }
    }

    @Override
    public List findAll() {
        List<Map<String,Object>> results = jdbcTemplate.queryForList("select * from nutrition");
        List<Nutrition> nutritions = new ArrayList<>();
        for(Map<String,Object> map : results){
            Nutrition nut = new Nutrition();
            System.out.println("....");
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println("key is " + entry.getKey());
                System.out.println("value is " + entry.getValue());
            }
            nut.setId((int)map.get("id"));
            nut.setCalories((int)map.get("calories"));
            nut.setCarbs((int)map.get("carbs"));
            nut.setProduct((String)map.get("product"));
            nutritions.add(nut);
        }
        System.out.println(results);
        return nutritions;
    }
}
