package com.example.controller;

import com.example.common.FoodGroup;
import com.example.domain.Nutrition;
import com.example.service.NutritionService;
import com.example.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by bsheen on 5/10/17.
 */

@Controller
public class NutritionController {

    private static final Logger logger = LoggerFactory.getLogger(NutritionController.class);

    @Autowired
    NutritionService nutritionService;

    @Autowired
    ProductService productService;

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
        model.addAttribute("productList",productService.findAll());
        return "nutrition";
    }

    @GetMapping("/nutrition/addproduct/{id}")
    public String addProductIdToNutrition(Model model, @PathVariable("id") Long product_id){
        Nutrition nutrition = new Nutrition();
        nutrition.setProductid(product_id);
        model.addAttribute("nutrition", nutrition);
        model.addAttribute("foodGroup", FoodGroup.values());
        return "nutritionForProduct";
    }

    @PostMapping("/nutrition/addproduct/{id}")
    public String submitAddProductIdToNutrition(Model model, @Valid Nutrition nutrition, BindingResult bindingResult, @PathVariable("id") Long product_id){
        if(bindingResult.hasErrors()){
            model.addAttribute("foodGroup", FoodGroup.values());
            model.addAttribute("nutrition", nutrition);
            return "nutritionForProductError";
        }
        nutritionService.add(nutrition);
        return "redirect:/product/"+product_id;
    }

    @PostMapping("/nutrition")
    public String nutritionSubmit(Model model, @Valid Nutrition nutrition, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            model.addAttribute("foodGroup", FoodGroup.values());
            model.addAttribute("productList",productService.findAll());
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
        model.addAttribute("productList",productService.findAll());
        return "edit";
    }

    @PostMapping("nutrition/edit/{id}")
    public String editSubmit(Model model, @Valid Nutrition nutrition, BindingResult bindingResult, @PathVariable("id") Long id){
        if(bindingResult.hasErrors()){
            model.addAttribute("foodGroup",FoodGroup.values());
            model.addAttribute("productList",productService.findAll());
            return "editError";
        }
        nutritionService.update(nutrition);
        model.addAttribute("nutrition",nutritionService.find(id));
        return "view";
    }

    @GetMapping("nutrition/editfromproduct/{id}")
    public String editFromProduct(Model model, @PathVariable("id") Long id){
        model.addAttribute("nutrition",nutritionService.find(id));
        model.addAttribute("foodGroup", FoodGroup.values());
        return "editFromProduct";
    }

    @PostMapping("nutrition/editfromproduct/{id}")
    public String submitEditFromProduct(Model model, @Valid Nutrition nutrition, BindingResult bindingResult, @PathVariable("id") Long id){
        if(bindingResult.hasErrors()){
            model.addAttribute("foodGroup",FoodGroup.values());
            return "editFromProductError";
        }
        nutritionService.update(nutrition);
        return "redirect:/product/"+nutrition.getProductid();
    }

    @PostMapping("/nutrition/delete/{id}")
    public String delete(Model model, @PathVariable("id") Long id){
        nutritionService.delete(id);
        model.addAttribute("nutritionList",nutritionService.findAll());
        return "redirect:/nutritions";
    }

    @PostMapping("/nutrition/deletefromproduct/{id}")
    public String deleteFromProduct(Model model, @PathVariable("id") Long id){
        Long product_id = nutritionService.find(id).getProductid();
        nutritionService.delete(id);
        return "redirect:/product/"+product_id;
    }

    @ExceptionHandler(value = Exception.class)
    public String handleGeneralException(Model model, final Exception exception, final HttpServletRequest request, final HttpServletResponse response){
        logger.warn(exception.getMessage() + "\n" + stackTraceAsString(exception));
        model.addAttribute("message", exception.getMessage());
        return "errorPage";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    private String stackTraceAsString(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }
}
