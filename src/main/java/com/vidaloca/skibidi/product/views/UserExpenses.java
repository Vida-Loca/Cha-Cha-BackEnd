package com.vidaloca.skibidi.product.views;

import com.vidaloca.skibidi.event.model.EventUser;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class UserExpenses {
    EventUser eventUser;
    BigDecimal expenses;
}
