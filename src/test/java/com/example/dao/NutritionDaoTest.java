package com.example.dao;

import java.util.List;
import java.util.Random;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.domain.Nutrition;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NutritionDaoTest {

    Random random = new Random();

    @Autowired
    NutritionDao nutritionDao;

    @Test
    public void testCreate() {
        Nutrition nutrition = new Nutrition();

        String product = Integer.toString(random.nextInt());
        nutrition.setProduct(product);

        int calories = random.nextInt();
        nutrition.setCalories(calories);

        int carbs = random.nextInt();
        nutrition.setCarbs(carbs);

        nutritionDao.add(nutrition);

        List<Nutrition> nutritions = nutritionDao.findAll();
        Assert.assertNotNull(nutritions);
        Assert.assertTrue(nutritions.size() > 0);
        boolean found = false;
        for (Nutrition nut : nutritions) {
            if (nut.equals(nutrition)) {
                found = true;
                break;
            }
        }

        Assert.assertTrue("Could not find " + nutrition, found);
    }
}
