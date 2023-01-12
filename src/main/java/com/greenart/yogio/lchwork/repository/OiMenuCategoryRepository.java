package com.greenart.yogio.lchwork.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.greenart.yogio.lchwork.entity.OiMenuCategoryEntity;

@Repository
public interface OiMenuCategoryRepository extends JpaRepository<OiMenuCategoryEntity, Long>{
    OiMenuCategoryEntity findByMcNameAndMcSiSeq(String mcName, Long mcSiSeq);
}
