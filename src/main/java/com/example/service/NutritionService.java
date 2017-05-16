package com.example.service;

import com.example.domain.Nutrition;

import java.util.List;

/**
 * Created by bsheen on 5/9/17.
 */
public interface NutritionService {

    int add(Nutrition nutrition);

    List<Nutrition> findAll();

    Nutrition find(long id);

    void update(Nutrition nutrition);

    void delete(long id);

    void add(List<Nutrition> nutritions);
}
