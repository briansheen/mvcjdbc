package com.example.service;

import com.example.domain.Nutrition;
import com.example.domain.Product;

import java.util.List;

/**
 * Created by bsheen on 5/21/17.
 */
public interface ProductService {

    int add(Product product);

    List<Product> findAll();

    List<Nutrition> findNutritionsWithProduct(long product_id);

    Product find(long product_id);

    void addProductToNutrition(Product product, Nutrition nutrition);

    void addProductToNutritions(List<Nutrition> nutritions, Product product);

    void removeProductFromNutrition(Nutrition nutrition);

    void removeProductFromNutritions(List<Nutrition> nutritions);

    void updateProduct(Product product);

    void deleteProduct(long product_id);

}
