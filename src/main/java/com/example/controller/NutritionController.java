package com.example.controller;

import com.example.domain.Nutrition;
import com.example.service.NutritionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

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

    @GetMapping("/nutrition")
    public String addNutrition(Model model) {
        model.addAttribute("nutrition", new Nutrition());
        return "nutrition";
    }

    @PostMapping("/nutrition")
    public String nutritionSubmit(Model model, @Valid Nutrition nutrition, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "nutritionError";
        }
        nutritionService.add(nutrition);
        model.addAttribute("nutritionList", nutritionService.findAll());
        return "nutritions";
    }

}
