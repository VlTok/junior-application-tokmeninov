package com.tokmeninov.spring.dto;

import com.tokmeninov.spring.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FindParams {
    private String expression;
    private Date afterCreateDate;
    private Date beforeCreateDate;
    private Boolean isComplete;
    private User author;
}
