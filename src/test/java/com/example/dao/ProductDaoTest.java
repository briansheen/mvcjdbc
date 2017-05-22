package com.example.dao;

import com.example.domain.Nutrition;
import com.example.domain.Product;
import groovy.transform.TailRecursive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bsheen on 5/19/17.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductDaoTest {

    Random random = new Random();

    @Autowired
    ProductDao productDao;

    @Autowired
    NutritionDao nutritionDao;

    @Test
    @Transactional
    public void testCreate() {
        Product product = createProduct();
        Assert.assertNotNull(product);
        productDao.add(product);
        Assert.assertNotNull(productDao.find(product.getId()));
    }

    @Test
    @Transactional
    public void testAddNutritionToProduct() {
        Product product = createProduct();
        productDao.add(product);
        NutritionDaoTest nutritionDaoTest = new NutritionDaoTest();
        Nutrition nutrition = nutritionDaoTest.createRandomNutrition();
        nutritionDao.add(nutrition);
        productDao.addProductToNutrition(product, nutrition);
        Assert.assertNotNull(nutritionDao.find(nutrition.getId()));
        Assert.assertEquals(product.getId(), nutritionDao.find(nutrition.getId()).getProductid().longValue());
    }

    @Test
    @Transactional
    public void testRemoveProductFromNutritions() {
        Product product = createProduct();
        productDao.add(product);
        List<Nutrition> nutritionList = createNutritionList();
        nutritionDao.add(nutritionList);
        productDao.addProductToNutritions(nutritionList, product);
        for (Nutrition nutrition : nutritionList) {
            Assert.assertEquals(product.getId(), nutritionDao.find(nutrition.getId()).getProductid().longValue());
        }
        productDao.removeProductFromNutritions(nutritionList);
        for (Nutrition nutrition : nutritionList) {
            Assert.assertEquals(null, nutritionDao.find(nutrition.getId()).getProductid());
        }
    }

    @Test
    @Transactional
    public void testDeleteProduct() {
        Product product = createProduct();
        productDao.add(product);
        List<Nutrition> nutritionList = createNutritionList();
        nutritionDao.add(nutritionList);
        productDao.addProductToNutritions(nutritionList,product);
        List<Nutrition> nutritionsWithProductAdded = productDao.findNutritionsWithProduct(product.getId());
        Assert.assertNotNull(nutritionsWithProductAdded);
        Assert.assertEquals(3,nutritionsWithProductAdded.size());
        productDao.removeProductFromNutritions(nutritionList);
        productDao.deleteProduct(product.getId());
        Assert.assertNull(productDao.find(product.getId()));
    }

    @Test
    @Transactional
    public void testUpdateProduct(){
        Product product = createProduct();
        productDao.add(product);
        product.setName("update");
        product.setBrand(null);
        productDao.updateProduct(product);
        Assert.assertEquals(product, productDao.find(product.getId()));
        product.setName("update2");
        product.setBrand("new");
        productDao.updateProduct (product);
        Assert.assertEquals(product, productDao.find(product.getId()));
    }

    public Product createProduct() {
        Product product = new Product();
        product.setName(String.valueOf(random.nextInt(2000)));
        product.setBrand(String.valueOf(random.nextInt(2000)));
        return product;
    }

    public List<Nutrition> createNutritionList() {
        NutritionDaoTest nutritionDaoTest = new NutritionDaoTest();
        Nutrition nutrition1 = nutritionDaoTest.createRandomNutrition();
        Nutrition nutrition2 = nutritionDaoTest.createRandomNutrition();
        Nutrition nutrition3 = nutritionDaoTest.createRandomNutrition();
        List<Nutrition> nutritionList = new ArrayList<>();
        nutritionList.add(nutrition1);
        nutritionList.add(nutrition2);
        nutritionList.add(nutrition3);
        return nutritionList;
    }
}
