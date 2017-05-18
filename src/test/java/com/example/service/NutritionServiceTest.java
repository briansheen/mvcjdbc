package com.example.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.example.common.FoodGroup;
import com.example.dao.NutritionDao;
import com.example.service.NutritionService;
import groovy.transform.TailRecursive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.domain.Nutrition;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NutritionServiceTest {

    Random random = new Random();

    @MockBean
    NutritionDao nutritionDao;

    @Autowired
    NutritionService nutritionService;

    @Test
    @Transactional
    public void testCreate() {
        Nutrition nutrition = createRandomNutrition();
        nutritionService.add(nutrition);
        Assert.assertNotNull(nutritionService.find(nutrition.getId()));
    }

    @Test
    @Transactional
    public void testUpdate() {
        Nutrition nutrition = createRandomNutrition();
        nutritionService.add(nutrition);

        Nutrition nutritionFromFind = nutritionService.find(nutrition.getId());
        Assert.assertEquals(nutrition, nutritionFromFind);

        Nutrition nutritionToUpdate = updateNutritionRandom(nutrition.getId());
        nutritionService.update(nutritionToUpdate);

        nutritionFromFind = nutritionService.find(nutrition.getId());
        Assert.assertEquals(nutritionToUpdate, nutritionFromFind);
    }

    @Test
    public void testDelete() {
        Nutrition nutrition = createRandomNutrition();
        nutritionService.add(nutrition);

        nutritionService.delete(nutrition.getId());
        Assert.assertNull(nutritionService.find(nutrition.getId()));
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
            nutritionService.add(nutritionList);
            Assert.fail("should not be able to add product string longer than 40 chars");
        } catch (DataIntegrityViolationException e) {
            Assert.assertTrue(true);
        }

        for(Nutrition nut: nutritionList) {
            Assert.assertNull(nutritionService.find(nut.getId()));
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

        nutritionService.add(nutritionList);

        for(Nutrition nut : nutritionList){
            Assert.assertNotNull(nutritionService.find(nut.getId()));
        }
    }

    @Test
    @Transactional
    public void testMockFind(){
        Nutrition nutrition = new Nutrition();
        nutrition.setId(226);
        nutrition.setGroup(FoodGroup.PROTEIN);
        nutrition.setProduct("meat test");

        org.mockito.BDDMockito.given(nutritionDao.find(226)).willReturn(nutrition);

        Nutrition nutritionReturn = nutritionService.find(227);
        Assert.assertNull(nutritionReturn);

        nutritionReturn = nutritionService.find(226);
        Assert.assertEquals(nutrition,nutritionReturn);
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
