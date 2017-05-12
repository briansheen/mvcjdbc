package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.common.FoodGroup;
import com.example.service.NutritionService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.domain.Nutrition;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NutritionServiceTest {

    Random random = new Random();

    @Autowired
    NutritionService nutritionService;

    @Test
    public void testCreate() {
        Nutrition nutrition = createRandomNutrition();
        nutritionService.add(nutrition);

        List<Nutrition> nutritions = nutritionService.findAll();

        Assert.assertNotNull(nutritions);
        Assert.assertTrue(nutritions.size() > 0);

        boolean found = false;
        found = findNutrition(nutritions, nutrition, found);

        Assert.assertTrue("Could not find " + nutrition, found);
    }

    @Test
    public void testUpdate() {
        Nutrition nutrition = createRandomNutrition();
        nutritionService.add(nutrition);

        List<Nutrition> nutritions = nutritionService.findAll();

        Nutrition foundNut = null;
        foundNut = findNutrition(nutritions, nutrition);

        Assert.assertNotNull(foundNut);
        Assert.assertTrue(foundNut.getId() > 0);

        Nutrition nutritionFromFind = nutritionService.find(foundNut.getId());
        Assert.assertEquals(foundNut, nutritionFromFind);

        Nutrition nutritionToUpdate = updateNutritionRandom(foundNut.getId());
        nutritionToUpdate.setProduct(null);
        try {
            nutritionService.update(nutritionToUpdate);
            Assert.fail("Should not be able to add a nutrition with null product");
        } catch (DataIntegrityViolationException e) {
            Assert.assertTrue(true);
        }

        nutritionFromFind = nutritionService.find(foundNut.getId());
        Assert.assertEquals(foundNut, nutritionFromFind);
    }

    @Test
    public void testDelete() {
        Nutrition nutrition = createRandomNutrition();
        nutritionService.add(nutrition);

        List<Nutrition> nutritions = nutritionService.findAll();

        Nutrition foundNut = null;
        foundNut = findNutrition(nutritions, nutrition);

        Assert.assertNotNull(foundNut);
        Assert.assertTrue(foundNut.getId() > 0);

        nutritionService.delete(foundNut.getId());

        boolean notFound = false;
        nutritions = nutritionService.findAll();
        notFound = findNutrition(nutritions, foundNut, notFound);

        Assert.assertFalse(notFound);
    }

    @Test
    public void testAdd() {
        Nutrition nutrition1 = createRandomNutrition();
        Nutrition nutrition2 = createRandomNutrition();
        nutrition2.setProduct("01234567890123456789012345678901234567890");

        Assert.assertNotEquals(nutrition1, nutrition2);

        List<Nutrition> nutritionList = new ArrayList<>();

        nutritionList.add(nutrition1);
        nutritionList.add(nutrition2);

        Assert.assertNotNull(nutritionList);

        try {
            nutritionService.add(nutritionList);
            Assert.fail("should not be able to add product string longer than 40 chars");
        } catch (DataIntegrityViolationException e) {
            Assert.assertTrue(true);
        }

        List<Nutrition> nutritions = nutritionService.findAll();

        boolean foundNut1 = false;
        foundNut1 = findNutrition(nutritions, nutrition1, foundNut1);

        boolean foundNut2 = false;
        foundNut2 = findNutrition(nutritions, nutrition2, foundNut2);

        Assert.assertFalse("Should not have found nutrition1", foundNut1);
        Assert.assertFalse("Should not have found nutrition2", foundNut2);
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
