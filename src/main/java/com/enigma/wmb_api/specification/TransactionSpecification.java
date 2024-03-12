package com.enigma.wmb_api.specification;

import com.enigma.wmb_api.dto.request.SearchTransactionRequest;
import com.enigma.wmb_api.entity.Customer;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.util.DateFormated;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class TransactionSpecification {

    public static Specification<Transaction> getSpecification(SearchTransactionRequest request) {

        return(root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if (request.getDate() != null) {
                Date startDate = DateFormated.parseDate(request.getDate(), "yyyy-MM-dd");
                Date endDate = new Timestamp(startDate.getTime() + 86400000);

                Predicate transDate = criteriaBuilder.between(root.get("transDate"), startDate, endDate);
                predicates.add(transDate);
            }

            if (request.getStartDate() != null || request.getEndDate() != null) {


                if (request.getStartDate() != null && request.getEndDate() != null) {
                    Date startDate = DateFormated.parseDate(request.getStartDate(), "yyyy-MM-dd");
                    Date endDate = DateFormated.parseDate(request.getEndDate(), "yyyy-MM-dd");
                    Predicate period = criteriaBuilder.between(root.get("transDate"), startDate, new Timestamp(endDate.getTime() + 86400000));
                    predicates.add(period);
                }
                else if (request.getStartDate() != null) {
                    Date startDate = DateFormated.parseDate(request.getStartDate(), "yyyy-MM-dd");
                    Predicate periodGreater = criteriaBuilder.greaterThan(root.get("transDate"),startDate);
                    predicates.add(periodGreater);
                }
                else {
                    Date endDate = DateFormated.parseDate(request.getEndDate(), "yyyy-MM-dd");
                    Predicate periodLess = criteriaBuilder.lessThan(root.get("transDate"), endDate);
                    predicates.add(periodLess);
                }
            }

            if (request.getCustomerId() != null) {
                Predicate customer = criteriaBuilder.equal(root.get("customer").get("id"), request.getCustomerId());
                predicates.add(customer);
            }

            if (request.getTransactionTypeId() != null) {

                Predicate transType = criteriaBuilder.equal(root.get("transType").get("id"), request.getTransactionTypeId());
                predicates.add(transType);
            }

            if (request.getTransactionStatus() != null) {
                Predicate transStatus = criteriaBuilder.equal(root.get("payment").get("transactionStatus"), request.getTransactionStatus());

                predicates.add(transStatus);
            }

            return query.where(
                    predicates.toArray(new Predicate[]{})
            ).getRestriction();

        };

    }

}
