package com.devrezaur.course.management.service.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.util.Date;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payment_info_table")
public class PaymentInfo {

    @Id
    @GeneratedValue
    @Column(name = "payment_id")
    private UUID paymentId;

    @Column(name = "course_id")
    private UUID courseId;

    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "payment_vendor")
    private String paymentVendor;

    @Column(name = "sender_account")
    private String senderAccount;

    @Column(name = "amount")
    private Integer amount;

    @Column(name = "trx_id")
    private String trxId;

    @CreationTimestamp
    @Column(name = "date", nullable = false, updatable = false)
    private Date date;

    @Column(name = "status")
    private String status;
}
