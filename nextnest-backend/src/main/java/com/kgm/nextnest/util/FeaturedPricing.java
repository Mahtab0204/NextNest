package com.kgm.nextnest.util;

import com.kgm.nextnest.model.FeaturedPlan;

public class FeaturedPricing {

    public static Double getPrice(
            FeaturedPlan plan
    ) {

        return switch (plan) {

            case SEVEN_DAYS -> 99.0;

            case FIFTEEN_DAYS -> 199.0;

            case THIRTY_DAYS -> 299.0;
        };
    }
}