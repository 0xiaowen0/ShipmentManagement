package com.pengw.demo.dao;

import com.pengw.demo.entity.Supplier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierDao  extends JpaRepository<Supplier,Long> {
}
