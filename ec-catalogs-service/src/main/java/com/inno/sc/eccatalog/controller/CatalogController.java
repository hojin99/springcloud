package com.inno.sc.eccatalog.controller;

import com.inno.sc.eccatalog.jpa.CatalogEntity;
import com.inno.sc.eccatalog.service.CatalogService;
import com.inno.sc.eccatalog.vo.ResponseCatalog;
import org.modelmapper.ModelMapper;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class CatalogController {

    CatalogService catalogService;
    Environment env;

    public CatalogController(CatalogService catalogService, Environment env) {
        this.catalogService = catalogService;
        this.env = env;
    }

    @GetMapping("/catalogs")
    public ResponseEntity<List<ResponseCatalog>> getCatalogs() {
        Iterable<CatalogEntity> orderList = catalogService.getAllCatalogs();

        List<ResponseCatalog> result = new ArrayList<>();
        orderList.forEach(v -> {
            result.add(new ModelMapper().map(v, ResponseCatalog.class));
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }



}
