package com.example.domain;

import com.example.common.FoodGroup;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Created by bsheen on 5/8/17.
 */
public class Nutrition {

    private long id;

    @NotNull
    @Size(min = 2, max = 50)
    private String product;

    @Min(0)
    @NotNull
    private int calories;

    @Min(0)
    @NotNull
    private int carbs;

    @NotNull
    private FoodGroup group;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public int getCalories() {
        return calories;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public int getCarbs() {
        return carbs;
    }

    public void setCarbs(int carbs) {
        this.carbs = carbs;
    }

    public FoodGroup getGroup() {
        return group;
    }

    public void setGroup(FoodGroup group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nutrition nutrition = (Nutrition) o;

        if (calories != nutrition.calories) return false;
        if (carbs != nutrition.carbs) return false;
        if (product != null ? !product.equals(nutrition.product) : nutrition.product != null) return false;
        return group == nutrition.group;
    }

    @Override
    public int hashCode() {
        int result = product != null ? product.hashCode() : 0;
        result = 31 * result + calories;
        result = 31 * result + carbs;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Nutrition{" +
                "id=" + id +
                ", product='" + product + '\'' +
                ", calories=" + calories +
                ", carbs=" + carbs +
                ", group=" + group +
                '}';
    }
}
