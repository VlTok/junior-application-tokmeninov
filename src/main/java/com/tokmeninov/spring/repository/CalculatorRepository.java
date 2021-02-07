package com.tokmeninov.spring.repository;

import com.tokmeninov.spring.model.Calculator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CalculatorRepository extends JpaRepository<Calculator,Long>, JpaSpecificationExecutor<Calculator> {
}
