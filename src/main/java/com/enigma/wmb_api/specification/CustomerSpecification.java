package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.SearchCustomerRequest;
import com.enigma.wmb_api.entity.Customer;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class CustomerSpecification {

    public static Specification<Customer> getSpecification(SearchCustomerRequest request) {

        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getName() != null) {
                Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + request.getName().toLowerCase() + "%");
                predicates.add(namePredicate);
            }

            if (request.getIsMember() != null) {
                Predicate isMemberPredicate = criteriaBuilder.equal(root.get("isMember"), request.getIsMember());
                predicates.add(isMemberPredicate);
            }

            return query.where(
                    predicates.toArray(new Predicate[]{})
            ).getRestriction();
        };

    }

}
