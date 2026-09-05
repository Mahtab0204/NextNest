package com.kgm.nextnest.specification;

import com.kgm.nextnest.model.Apartment;
import com.kgm.nextnest.model.Purpose;
import org.springframework.data.jpa.domain.Specification;

public class ApartmentSpecification {

    public static Specification<Apartment> filterApartments(
            Purpose purpose,
            Integer bedrooms,
            Double minPrice,
            Double maxPrice
    ) {

        return (root, query, cb) -> {

            var predicate = cb.conjunction();

            if (purpose != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("purpose"),
                                purpose
                        )
                );
            }

            if (bedrooms != null) {

                predicate = cb.and(
                        predicate,
                        cb.equal(
                                root.get("bedrooms"),
                                bedrooms
                        )
                );
            }

            if (minPrice != null) {

                predicate = cb.and(
                        predicate,
                        cb.greaterThanOrEqualTo(
                                root.get("price"),
                                minPrice
                        )
                );
            }

            if (maxPrice != null) {

                predicate = cb.and(
                        predicate,
                        cb.lessThanOrEqualTo(
                                root.get("price"),
                                maxPrice
                        )
                );
            }

            return predicate;
        };
    }
}