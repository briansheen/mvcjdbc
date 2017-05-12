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
        Nutrition nutrition = createRandomNutrition();
        nutritionDao.add(nutrition);

        List<Nutrition> nutritions = nutritionDao.findAll();

        Assert.assertNotNull(nutritions);
        Assert.assertTrue(nutritions.size() > 0);

        boolean found = false;
        found = findNutrition(nutritions, nutrition, found);

        Assert.assertTrue("Could not find " + nutrition, found);
    }

    @Test
    public void testUpdate() {
        Nutrition nutrition = createRandomNutrition();
        nutritionDao.add(nutrition);

        List<Nutrition> nutritions = nutritionDao.findAll();

        Nutrition foundNut = null;
        foundNut = findNutrition(nutritions, nutrition);

        Assert.assertNotNull(foundNut);
        Assert.assertTrue(foundNut.getId() > 0);

        Nutrition nutritionFromFind = nutritionDao.find(foundNut.getId());
        Assert.assertEquals(foundNut, nutritionFromFind);

        Nutrition nutritionToUpdate = updateNutritionRandom(foundNut.getId());

        nutritionDao.update(nutritionToUpdate);
        Nutrition nutritionAfterUpdate = nutritionDao.find(nutritionToUpdate.getId());

        Assert.assertEquals(nutritionToUpdate, nutritionAfterUpdate);
    }

    @Test
    public void testDelete() {
        Nutrition nutrition = createRandomNutrition();
        nutritionDao.add(nutrition);

        List<Nutrition> nutritions = nutritionDao.findAll();

        Nutrition foundNut = null;
        foundNut = findNutrition(nutritions, nutrition);

        Assert.assertNotNull(foundNut);
        Assert.assertTrue(foundNut.getId() > 0);

        nutritionDao.delete(foundNut.getId());

        boolean notFound = false;
        nutritions = nutritionDao.findAll();
        notFound = findNutrition(nutritions, foundNut, notFound);

        Assert.assertFalse(notFound);

        Assert.assertNull(nutritionDao.find(foundNut.getId()));
    }

    @Test
    public void testAdd() {
        Nutrition nutrition1 = createRandomNutrition();
        Nutrition nutrition2 = createRandomNutrition();

        Assert.assertNotEquals(nutrition1, nutrition2);

        List<Nutrition> nutritionList = new ArrayList<>();

        nutritionList.add(nutrition1);
        nutritionList.add(nutrition2);

        Assert.assertNotNull(nutritionList);

        nutritionDao.add(nutritionList);

        List<Nutrition> nutritions = nutritionDao.findAll();

        Nutrition foundNut1 = findNutrition(nutritions, nutrition1);
        Nutrition afterFindNut1 = nutritionDao.find(foundNut1.getId());
        Assert.assertEquals(foundNut1, afterFindNut1);

        Nutrition foundNut2 = findNutrition(nutritions, nutrition2);
        Nutrition afterFindNut2 = nutritionDao.find(foundNut2.getId());
        Assert.assertEquals(foundNut2, afterFindNut2);
    }

    private Nutrition createRandomNutrition() {
        Nutrition nutrition = new Nutrition();

        String product = Integer.toString(random.nextInt(3000));
        nutrition.setProduct(product);

        int calories = random.nextInt(3000);
        nutrition.setCalories(calories);

        int carbs = random.nextInt(3000);
        nutrition.setCarbs(carbs);

        nutrition.setGroup(FoodGroup.values()[random.nextInt(FoodGroup.values().length)]);

        return nutrition;
    }

    private Nutrition updateNutritionRandom(long id) {
        Nutrition nutrition = new Nutrition();
        nutrition.setProduct(Integer.toString(random.nextInt(3000)));
        nutrition.setCalories(random.nextInt(3000));
        nutrition.setCarbs(random.nextInt(3000));
        nutrition.setGroup(FoodGroup.values()[random.nextInt(FoodGroup.values().length)]);
        nutrition.setId(id);
        return nutrition;
    }

    private Nutrition findNutrition(List<Nutrition> nutritions, Nutrition nutrition) {
        for (Nutrition nut : nutritions) {
            if (nut.equals(nutrition)) {
                nutrition = nut;
                break;
            }
        }
        return nutrition;
    }

    private boolean findNutrition(List<Nutrition> nutritions, Nutrition nutrition, boolean found) {
        for (Nutrition nut : nutritions) {
            if (nut.equals(nutrition)) {
                found = true;
                break;
            }
        }
        return found;
    }

}
