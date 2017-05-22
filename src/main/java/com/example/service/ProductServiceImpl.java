package com.example.service;

import com.example.dao.ProductDao;
import com.example.domain.Nutrition;
import com.example.domain.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by bsheen on 5/21/17.
 */

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    ProductDao productDao;

    @Override
    @Transactional
    public int add(Product product) {
        return productDao.add(product);
    }

    @Override
    public List<Product> findAll() {
        return productDao.findAll();
    }

    @Override
    public List<Nutrition> findNutritionsWithProduct(long product_id) {
        return productDao.findNutritionsWithProduct(product_id);
    }

    @Override
    public Product find(long product_id) {
        return productDao.find(product_id);
    }

    @Override
    public List<Long> findAllIds() {
        return productDao.findAllIds();
    }

    @Override
    @Transactional
    public void addProductToNutrition(Product product, Nutrition nutrition) {
        productDao.addProductToNutrition(product, nutrition);
    }

    @Override
    @Transactional
    public void addProductToNutritions(List<Nutrition> nutritions, Product product) {
        productDao.addProductToNutritions(nutritions, product);
    }

    @Override
    @Transactional
    public void removeProductFromNutrition(Nutrition nutrition) {
        productDao.removeProductFromNutrition(nutrition);
    }

    @Override
    @Transactional
    public void removeProductFromNutritions(List<Nutrition> nutritions) {
        productDao.removeProductFromNutritions(nutritions);
    }

    @Override
    @Transactional
    public void updateProduct(Product product) {
        productDao.updateProduct(product);
    }

    @Override
    @Transactional
    public void deleteProduct(long product_id) {
        productDao.deleteProduct(product_id);
    }
}
