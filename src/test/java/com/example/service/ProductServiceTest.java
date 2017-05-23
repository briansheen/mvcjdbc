package com.example.service;

import com.example.dao.NutritionDao;
import com.example.dao.NutritionDaoTest;
import com.example.dao.ProductDao;
import com.example.domain.Nutrition;
import com.example.domain.Product;
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
 * Created by bsheen on 5/22/17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTest {
    Random random = new Random();

    @Autowired
    ProductService productService;

    @Autowired
    NutritionService nutritionService;

    @Test
    @Transactional
    public void testCreate() {
        Product product = createProduct();
        Assert.assertNotNull(product);
        productService.add(product);
        Assert.assertNotNull(productService.find(product.getId()));
    }

    @Test
    @Transactional
    public void testAddNutritionToProduct() {
        Product product = createProduct();
        productService.add(product);
        NutritionServiceTest nutritionServiceTest = new NutritionServiceTest();
        Nutrition nutrition = nutritionServiceTest.createRandomNutrition();
        nutritionService.add(nutrition);
        productService.addProductToNutrition(product, nutrition);
        Assert.assertNotNull(nutritionService.find(nutrition.getId()));
        Assert.assertEquals(product.getId(), nutritionService.find(nutrition.getId()).getProductid().longValue());
    }

    @Test
    @Transactional
    public void testRemoveProductFromNutritions() {
        Product product = createProduct();
        productService.add(product);
        List<Nutrition> nutritionList = createNutritionList();
        nutritionService.add(nutritionList);
        productService.addProductToNutritions(nutritionList, product);
        for (Nutrition nutrition : nutritionList) {
            Assert.assertEquals(product.getId(), nutritionService.find(nutrition.getId()).getProductid().longValue());
        }
        productService.removeProductFromNutritions(nutritionList);
        for (Nutrition nutrition : nutritionList) {
            Assert.assertEquals(null, nutritionService.find(nutrition.getId()).getProductid());
        }
    }

    @Test
    @Transactional
    public void testDeleteProduct() {
        Product product = createProduct();
        productService.add(product);
        List<Nutrition> nutritionList = createNutritionList();
        nutritionService.add(nutritionList);
        productService.addProductToNutritions(nutritionList,product);
        List<Nutrition> nutritionsWithProductAdded = productService.findNutritionsWithProduct(product.getId());
        Assert.assertNotNull(nutritionsWithProductAdded);
        Assert.assertEquals(3,nutritionsWithProductAdded.size());
        productService.removeProductFromNutritions(nutritionList);
        productService.deleteProduct(product.getId());
        Assert.assertNull(productService.find(product.getId()));
    }

    @Test
    @Transactional
    public void testUpdateProduct(){
        Product product = createProduct();
        productService.add(product);
        product.setName("update");
        product.setBrand(null);
        productService.updateProduct(product);
        Assert.assertEquals(product, productService.find(product.getId()));
        product.setName("update2");
        product.setBrand("new");
        productService.updateProduct (product);
        Assert.assertEquals(product, productService.find(product.getId()));
    }

    public Product createProduct() {
        Product product = new Product();
        product.setName(String.valueOf(random.nextInt(2000)));
        product.setBrand(String.valueOf(random.nextInt(2000)));
        return product;
    }

    public List<Nutrition> createNutritionList() {
        NutritionServiceTest nutritionServiceTest = new NutritionServiceTest();
        Nutrition nutrition1 = nutritionServiceTest.createRandomNutrition();
        Nutrition nutrition2 = nutritionServiceTest.createRandomNutrition();
        Nutrition nutrition3 = nutritionServiceTest.createRandomNutrition();
        List<Nutrition> nutritionList = new ArrayList<>();
        nutritionList.add(nutrition1);
        nutritionList.add(nutrition2);
        nutritionList.add(nutrition3);
        return nutritionList;
    }
}
