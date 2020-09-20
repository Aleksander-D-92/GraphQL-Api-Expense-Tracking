package com.expence_tracking.app.dto.binding.transaction;

import com.expence_tracking.app.domain.enums.TransactionType;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ExpenseIncomeForm
{
    private String note;
    private LocalDateTime date;
    private TransactionType type;
    private BigDecimal balance;
    private String subCategoryName; //possible null
    private Long categoryId; //possible null
    private Long bankAccountId;
}
