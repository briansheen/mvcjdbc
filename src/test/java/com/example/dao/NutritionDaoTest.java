package com.example.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.common.FoodGroup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.domain.Nutrition;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NutritionDaoTest {

    Random random = new Random();

    @Autowired
    NutritionDao nutritionDao;

    @Test
    @Transactional
    public void testCreate() {
        Nutrition nutrition = createRandomNutrition();
        nutritionDao.add(nutrition);
        Assert.assertNotNull(nutritionDao.find(nutrition.getId()));
    }

    @Test
    @Transactional
    public void testUpdate() {
        Nutrition nutrition = createRandomNutrition();
        nutritionDao.add(nutrition);

        Nutrition nutritionFromFind = nutritionDao.find(nutrition.getId());
        Assert.assertEquals(nutrition, nutritionFromFind);

        Nutrition nutritionToUpdate = updateNutritionRandom(nutrition.getId());
        nutritionDao.update(nutritionToUpdate);

        nutritionFromFind = nutritionDao.find(nutrition.getId());
        Assert.assertEquals(nutritionToUpdate, nutritionFromFind);
    }

    @Test
    @Transactional
    public void testDelete() {
        Nutrition nutrition = createRandomNutrition();
        nutritionDao.add(nutrition);

        nutritionDao.delete(nutrition.getId());
        Assert.assertNull(nutritionDao.find(nutrition.getId()));
    }

    @Test
    public void testCannotAdd() {
        Nutrition nutrition1 = createRandomNutrition();
        Nutrition nutrition2 = createRandomNutrition();
        nutrition2.setProduct("01234567890123456789012345678901234567890");

        Assert.assertNotEquals(nutrition1, nutrition2);

        List<Nutrition> nutritionList = new ArrayList<>();

        nutritionList.add(nutrition1);
        nutritionList.add(nutrition2);

        Assert.assertNotNull(nutritionList);

        try {
            nutritionDao.add(nutritionList);
            Assert.fail("should not be able to add product string longer than 40 chars");
        } catch (DataIntegrityViolationException e) {
            Assert.assertTrue(true);
        }

        for(Nutrition nut: nutritionList) {
            Assert.assertNull(nutritionDao.find(nut.getId()));
        }
    }

    @Test
    @Transactional
    public void testAddMultiple() {
        Nutrition nutrition1 = createRandomNutrition();
        Nutrition nutrition2 = createRandomNutrition();

        Assert.assertNotEquals(nutrition1,nutrition2);

        List<Nutrition> nutritionList = new ArrayList<>();
        nutritionList.add(nutrition1);
        nutritionList.add(nutrition2);

        Assert.assertNotNull(nutritionList);
        Assert.assertTrue(nutritionList.size() > 0);

        nutritionDao.add(nutritionList);

        for(Nutrition nut : nutritionList){
            Assert.assertNotNull(nutritionDao.find(nut.getId()));
        }
    }

    private Nutrition createRandomNutrition() {
        Nutrition nutrition = new Nutrition();

        String product = Integer.toString(random.nextInt(3000));
        nutrition.setProduct(product);

        int calories = random.nextInt(3000);
        nutrition.setCalories(calories);

        int carbs = random.nextInt(3000);
        nutrition.setCarbs(carbs);

        int group = random.nextInt(6);
        if (group == 0) {
            nutrition.setGroup(null);
        } else {
            nutrition.setGroup(FoodGroup.values()[random.nextInt(FoodGroup.values().length)]);
        }

        int bool = random.nextInt(3);
        if (bool == 0) {
            nutrition.setFavorite(null);
        } else {
            nutrition.setFavorite(random.nextBoolean());
        }

        return nutrition;
    }

    private Nutrition updateNutritionRandom(long id) {
        Nutrition nutrition = new Nutrition();
        nutrition.setProduct(Integer.toString(random.nextInt(3000)));
        nutrition.setCalories(random.nextInt(3000));
        nutrition.setCarbs(random.nextInt(3000));
        int group = random.nextInt(6);
        if (group == 0) {
            nutrition.setGroup(null);
        } else {
            nutrition.setGroup(FoodGroup.values()[random.nextInt(FoodGroup.values().length)]);
        }

        int bool = random.nextInt(3);
        if (bool == 0) {
            nutrition.setFavorite(null);
        } else {
            nutrition.setFavorite(random.nextBoolean());
        }
        nutrition.setId(id);
        return nutrition;
    }
}
