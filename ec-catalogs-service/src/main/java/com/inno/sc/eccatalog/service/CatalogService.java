package com.inno.sc.eccatalog.service;

import com.inno.sc.eccatalog.jpa.CatalogEntity;

public interface CatalogService {
    Iterable<CatalogEntity> getAllCatalogs();
}
