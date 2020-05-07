package com.vidaloca.skibidi.product.service;

import java.math.BigDecimal;

public interface ProductValueService {
    BigDecimal totalAmountOfEvent(Long eventId, Long userId);

    BigDecimal totalAmountOfProduct(Long productId, Long eventId, Long userId);

    BigDecimal totalAmountOfCurrentUser (Long eventId, Long userId);

    BigDecimal totalAmountOfProductCategory(Long productCategoryId,Long eventId, Long userId);

    BigDecimal totalAmountOfEventUser (Long eventId, Long userId, Long currentUserId);
}
