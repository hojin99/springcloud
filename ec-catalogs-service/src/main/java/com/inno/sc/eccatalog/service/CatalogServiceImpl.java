package com.inno.sc.eccatalog.service;

import com.inno.sc.eccatalog.jpa.CatalogEntity;
import com.inno.sc.eccatalog.jpa.CatalogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class CatalogServiceImpl implements CatalogService{

    CatalogRepository repository;
    Environment env;

    public CatalogServiceImpl(CatalogRepository repository, Environment env) {
        this.repository = repository;
        this.env = env;

    }

    @Override
    public Iterable<CatalogEntity> getAllCatalogs() {
        return repository.findAll();
    }
}
