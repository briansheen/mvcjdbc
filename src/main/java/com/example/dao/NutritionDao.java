package com.example.dao;

import com.example.domain.Nutrition;

import java.util.List;

/**
 * Created by bsheen on 5/8/17.
 */
public interface NutritionDao {

    void add(Nutrition nutrition);
    List<Nutrition> findAll();

}
