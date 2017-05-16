package com.example.controller;

import com.example.common.FoodGroup;
import com.example.domain.Nutrition;
import com.example.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bsheen on 5/10/17.
 */

@Controller
public class NutritionController {

    @Autowired
    NutritionService nutritionService;

    @GetMapping("/nutritions")
    public String allNutritions(Model model) {
        model.addAttribute("nutritionList", nutritionService.findAll());
        return "nutritions";
    }

    @PostMapping("/nutritions")
    public String deleteNutritions(Model model, @RequestParam(value="deleteMe", required = true) List<Long> nutritionIds){
        for(Long nId:nutritionIds){
            nutritionService.delete(nId);
        }
        model.addAttribute("nutritionList",nutritionService.findAll());
        return "nutritions";
    }

    @GetMapping("/nutrition")
    public String addNutrition(Model model) {
        model.addAttribute("nutrition", new Nutrition());
        model.addAttribute("foodGroup", FoodGroup.values());
        return "nutrition";
    }

    @PostMapping("/nutrition")
    public String nutritionSubmit(Model model, @Valid Nutrition nutrition, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            model.addAttribute("foodGroup", FoodGroup.values());
            return "nutritionError";
        }
        nutritionService.add(nutrition);
        model.addAttribute("nutritionList", nutritionService.findAll());
        return "nutritions";
    }

    @GetMapping("/nutrition/{id}")
    public String view(Model model, @PathVariable("id") Long id){
        model.addAttribute("nutrition",nutritionService.find(id));
        return "view";
    }

    @GetMapping("nutrition/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id){
        model.addAttribute("nutrition",nutritionService.find(id));
        model.addAttribute("foodGroup", FoodGroup.values());
        return "edit";
    }

    @PostMapping("nutrition/edit/{id}")
    public String editSubmit(Model model, @Valid Nutrition nutrition, BindingResult bindingResult, @PathVariable("id") Long id){
        if(bindingResult.hasErrors()){
            model.addAttribute("foodGroup",FoodGroup.values());
            return "editError";
        }
        nutritionService.update(nutrition);
        model.addAttribute("nutrition",nutritionService.find(id));
        return "view";
    }

    @GetMapping("/nutrition/delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id){
        nutritionService.delete(id);
        model.addAttribute("nutritionList",nutritionService.findAll());
        return "redirect:/nutritions";
    }
}
