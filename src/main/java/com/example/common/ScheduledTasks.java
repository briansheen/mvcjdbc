package com.example.common;

import com.example.domain.Product;
import com.example.service.ProductService;
import com.example.service.ProductServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Random;

/**
 * Created by bsheen on 5/23/17.
 */

@Component
public class ScheduledTasks {

    @Autowired
    ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");


    @Scheduled(fixedRate = 60000)
    public void logTheTime(){
        logger.info("The time is {}", dateFormat.format(new Date()));
    }

    @Scheduled(cron="0 0 12 * * *")
    public void logMay(){
        LocalDateTime localDateTime = LocalDateTime.now();
        Product product = new Product();
        product.setName(String.valueOf(System.currentTimeMillis()));
        product.setBrand(String.valueOf(localDateTime.getDayOfWeek().getValue()));
        productService.add(product);
        logger.info("Added product {}", product);
    }

}
