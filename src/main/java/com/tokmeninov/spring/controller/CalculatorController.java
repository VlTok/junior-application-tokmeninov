package com.tokmeninov.spring.controller;

import com.tokmeninov.spring.dto.FindParams;
import com.tokmeninov.spring.model.Calculator;
import com.tokmeninov.spring.model.User;
import com.tokmeninov.spring.service.CalculatorService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.script.ScriptException;
import java.util.List;

@RestController
@RequestMapping("/calculator")
@AllArgsConstructor
public class CalculatorController {

    private final CalculatorService calculatorService;

    @GetMapping
    List<Calculator> findAll() {
        return calculatorService.findAll();
    }

    @GetMapping("/{id}")
    Calculator findById(@PathVariable Long id) {
        return calculatorService.findById(id);
    }

    @PostMapping
    String add(@RequestBody Calculator calculator, @AuthenticationPrincipal User user) throws ScriptException, NullPointerException {
        calculator.setAuthor(user);
        Calculator newCalculator = calculatorService.add(calculator);
        return newCalculator.getResult();
    }

    @PutMapping("/{id}")
    Calculator update(@RequestBody Calculator calculator, @PathVariable Long id) throws ScriptException {
        return calculatorService.update(calculator, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        calculatorService.delete(id);
    }

    @PostMapping("/findByParam")
    public List<Calculator> findByParam(@RequestBody FindParams findParams){
        return calculatorService.findByParam(findParams);
    }
}
