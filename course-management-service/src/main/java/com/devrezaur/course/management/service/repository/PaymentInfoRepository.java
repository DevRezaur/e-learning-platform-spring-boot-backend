package com.devrezaur.course.management.service.repository;

import com.devrezaur.course.management.service.model.PaymentInfo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PaymentInfoRepository extends JpaRepository<PaymentInfo, UUID> {

    List<PaymentInfo> findAllBy(PageRequest pageable);

    PaymentInfo findByTrxId(String trxId);
}
