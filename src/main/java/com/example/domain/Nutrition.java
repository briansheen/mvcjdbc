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
    @Size(min = 2, max = 40)
    private String product;

    @Min(0)
    @NotNull
    private int calories;

    @Min(0)
    @NotNull
    private int carbs;

    private FoodGroup group;

    private Boolean favorite;

    private Long productid;

    public Long getProductid() {
        return productid;
    }

    public void setProductid(Long productid) {
        this.productid = productid;
    }

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

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Nutrition nutrition = (Nutrition) o;

        if (id != nutrition.id) return false;
        if (calories != nutrition.calories) return false;
        if (carbs != nutrition.carbs) return false;
        if (product != null ? !product.equals(nutrition.product) : nutrition.product != null) return false;
        if (group != nutrition.group) return false;
        if (favorite != null ? !favorite.equals(nutrition.favorite) : nutrition.favorite != null) return false;
        return productid != null ? productid.equals(nutrition.productid) : nutrition.productid == null;
    }

    @Override
    public int hashCode() {
        int result = (int) (id ^ (id >>> 32));
        result = 31 * result + (product != null ? product.hashCode() : 0);
        result = 31 * result + calories;
        result = 31 * result + carbs;
        result = 31 * result + (group != null ? group.hashCode() : 0);
        result = 31 * result + (favorite != null ? favorite.hashCode() : 0);
        result = 31 * result + (productid != null ? productid.hashCode() : 0);
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
                ", favorite=" + favorite +
                ", productid=" + productid +
                '}';
    }
}

