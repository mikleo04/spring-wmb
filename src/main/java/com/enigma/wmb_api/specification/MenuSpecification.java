package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.SearchMenuRequest;
import com.enigma.wmb_api.entity.Menu;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class MenuSpecification {
    public static Specification<Menu> getSpecification(SearchMenuRequest request) {

        return(root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null) {
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
                predicates.add(namePredicate);
            }

            if (request.getStatus() != null) {
                Predicate statusPredicate = criteriaBuilder.equal(root.get("status"), request.getStatus());
                predicates.add(statusPredicate);
            }

            if (request.getMinPrice() != null || request.getMaxPrice() != null) {
                if (request.getMinPrice() != null && request.getMaxPrice() != null) {
                    Predicate priceBetween = criteriaBuilder.between(root.get("price"), request.getMinPrice(), request.getMaxPrice());
                    predicates.add(priceBetween);
                }
                else if (request.getMinPrice() != null) {
                    Predicate minPrice = criteriaBuilder.greaterThanOrEqualTo(root.get("price"),request.getMinPrice());
                    predicates.add(minPrice);
                }
                else {
                    Predicate maxPrice = criteriaBuilder.lessThanOrEqualTo(root.get("price"), request.getMaxPrice());
                    predicates.add(maxPrice);
                }
            }

            return query.where(
                    predicates.toArray(new Predicate[]{})
            ).getRestriction();
        };

    }
}
