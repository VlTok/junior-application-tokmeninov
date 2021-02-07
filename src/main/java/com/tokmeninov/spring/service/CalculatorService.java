package com.tokmeninov.spring.service;


import com.tokmeninov.spring.dto.FindParams;
import com.tokmeninov.spring.exception.NotFoundException;
import com.tokmeninov.spring.model.Calculator;
import com.tokmeninov.spring.model.User;
import com.tokmeninov.spring.repository.CalculatorRepository;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

@Service
@Transactional
@AllArgsConstructor
public class CalculatorService {

    private final CalculatorRepository calculatorRepository;

    public List<Calculator> findAll() {
        return calculatorRepository.findAll();
    }

    public Calculator findById(Long id) {
        return calculatorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Calculator add(Calculator calculator) throws ScriptException {
        calculator.setResult(calculatingResult(calculator.getExpression()));
        calculator.setCreateDate(new Date());
        calculator.setUpdateDate(null);
        if (calculator.getResult().equals("Деление на ноль")){
            return calculator;
        }
        return calculatorRepository.save(calculator);
    }

    private String calculatingResult(String expression) throws ScriptException {
        String result;
        ScriptEngineManager sem = new ScriptEngineManager();
        ScriptEngine eng = sem.getEngineByName("JavaScript");
        result = eng.eval(expression).toString();
        try {
            if (result.equals("-Infinity")) {
                result="Деление на ноль";
                throw new ArithmeticException("Cannot be divide by zero!");
            }
        }catch (ArithmeticException e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    public Calculator update(Calculator calculator, Long id) throws ScriptException {
        Calculator saveCalculator = findById(id);

        if (calculator.getExpression() != null) {
            saveCalculator.setExpression(calculator.getExpression());
            saveCalculator.setResult(calculatingResult(calculator.getExpression()));
        }
        if (calculator.getIsComplete() != null) {
            saveCalculator.setIsComplete(calculator.getIsComplete());
        }
        saveCalculator.setUpdateDate(new Date());
        return calculatorRepository.save(saveCalculator);
    }

    public void delete(Long id) {
        Calculator calculator = findById(id);
        calculatorRepository.delete(calculator);
    }
    public List<Calculator> findByParam(FindParams findParams) {
        return calculatorRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {

            criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createDate")));

            List<Predicate> predicates = new ArrayList<>();

            if (findParams.getAuthor()!=null){
                    predicates.add(criteriaBuilder.equal(root.get("author"), findParams.getAuthor()));
            }

            if (findParams.getIsComplete() != null) {
                predicates.add(criteriaBuilder.equal(
                        root.get("isComplete"),
                        findParams.getIsComplete()));
            }

            if (findParams.getAfterCreateDate() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createDate"), findParams.getAfterCreateDate()));
            }

            if (findParams.getBeforeCreateDate() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createDate"), findParams.getBeforeCreateDate()));
            }

            //Поиск по выражению
            if (StringUtils.isNotEmpty(findParams.getExpression())) {
                Stream.of(findParams
                        .getExpression()
                        .trim()
                        .split("[^\\d*\\W\\d*]"))
                        .forEach(key -> {
                            if (key.length() >= 3) {
                                predicates.add(criteriaBuilder.like(criteriaBuilder.upper(root.get("expression")), "%" + key + "%"));
                            }
                        });
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[]{}));
        });
    }
}
