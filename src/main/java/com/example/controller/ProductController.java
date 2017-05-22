package com.example.controller;

import com.example.domain.Nutrition;
import com.example.domain.Product;
import com.example.service.NutritionService;
import com.example.service.ProductService;
import com.sun.org.apache.xpath.internal.operations.Mod;
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
import java.util.List;

/**
 * Created by bsheen on 5/19/17.
 */

@Controller
public class ProductController {
    private static final Logger logger = LoggerFactory.getLogger(NutritionController.class);

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public String allProducts(Model model){
        model.addAttribute("productList", productService.findAll());
        return "products";
    }

    @PostMapping("/products")
    public String deleteProducts(Model model, @RequestParam(value="deleteMe",required = true) List<Long> productIds){
        for(Long pId : productIds){
            List<Nutrition> nutritions = productService.findNutritionsWithProduct(pId);
            productService.removeProductFromNutritions(nutritions);
            productService.deleteProduct(pId);
        }
        model.addAttribute("productList", productService.findAll());
        return "products";
    }

    @GetMapping("/product")
    public String addProduct(Model model){
        model.addAttribute("product", new Product());
        return "product";
    }

    @PostMapping("/product")
    public String addProductSubmit(Model model, @Valid Product product, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "productError";
        }
        productService.add(product);
        System.out.println("!!!!\n"+product.getId());
        model.addAttribute(productService.findAll());
        return "products";
    }

    @GetMapping("/product/{id}")
    public String view(Model model, @PathVariable("id") Long product_id){
        model.addAttribute("product",productService.find(product_id));
        model.addAttribute("productNutritions", productService.findNutritionsWithProduct(product_id));
        return "viewProduct";
    }

    @PostMapping("/product/delete/{id}")
    public String deleteProduct(Model model, @PathVariable("id") Long product_id){
        List<Nutrition> productNutritions = productService.findNutritionsWithProduct(product_id);
        productService.removeProductFromNutritions(productNutritions);
        productService.deleteProduct(product_id);
        model.addAttribute("productList", productService.findAll());
        return "redirect:/products";
    }

    @GetMapping("/product/edit/{id}")
    public String editProduct(Model model, @PathVariable("id") Long product_id){
        model.addAttribute("product", productService.find(product_id));
        System.out.println("!!!\n"+productService.find(product_id));
        model.addAttribute("productNutritions",productService.findNutritionsWithProduct(product_id));
        return "editProduct";
    }

    @PostMapping("/product/edit/{id}")
    public String editProductSubmit(Model model, @Valid Product product, BindingResult bindingResult, @PathVariable("id") Long product_id){
        if(bindingResult.hasErrors()){
            return "editProductError";
        }
        System.out.println("!!!!\n"+product.getId());
        System.out.println("!!!!\n"+product_id);
        productService.updateProduct(product);
        model.addAttribute("product", productService.find(product_id));
        model.addAttribute("productNutritions", productService.findNutritionsWithProduct(product_id));
        return "viewProduct";
    }

    @ExceptionHandler(value = Exception.class)
    public String handleGeneralException(Model model, final Exception exception, final HttpServletRequest request, final HttpServletResponse response){
        logger.warn(exception.getMessage() + "\n" + stackTraceAsString(exception));
        model.addAttribute("message", exception.getMessage());
        return "errorPageProduct";
    }

    private String stackTraceAsString(Exception exception) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        exception.printStackTrace(pw);
        return sw.toString();
    }

}
