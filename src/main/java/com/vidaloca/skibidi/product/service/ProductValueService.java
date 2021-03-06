package com.vidaloca.skibidi.product.service;

import com.vidaloca.skibidi.product.views.UserExpenses;

import java.math.BigDecimal;
import java.util.List;

public interface ProductValueService {
    BigDecimal totalAmountOfEvent(Long eventId, Long userId);

    BigDecimal totalAmountOfProduct(Long productId, Long eventId, Long userId);

    BigDecimal totalAmountOfCurrentUser (Long eventId, Long userId);

    BigDecimal totalAmountOfProductCategory(Long productCategoryId,Long eventId, Long userId);

    BigDecimal totalAmountOfEventUser (Long eventId, Long userId, Long currentUserId);

    List<UserExpenses> totalUsersExpenses (Long eventId, Long userId);
}
