package com.tokmeninov.spring.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@ToString(of = {"id", "expression"})
@EqualsAndHashCode(of = {"id"})
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Calculator {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String expression;
    private String result;
    private Boolean isComplete;
    @Column(updatable = false)
    private Date createDate;
    private Date updateDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User author;
}
