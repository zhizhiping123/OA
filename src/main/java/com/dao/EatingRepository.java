package com.dao;

import com.entity.Eating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EatingRepository extends JpaRepository<Eating,Long>,JpaSpecificationExecutor<Eating>{
}
