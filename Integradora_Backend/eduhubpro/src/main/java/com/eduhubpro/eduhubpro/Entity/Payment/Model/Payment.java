package com.eduhubpro.eduhubpro.Entity.Payment.Model;

import com.eduhubpro.eduhubpro.Entity.Account.Model.Account;
import com.eduhubpro.eduhubpro.Entity.Registration.Model.Registration;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.PaymentStatus;
import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "payment")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID paymentId;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PaymentStatus status = PaymentStatus.PENDING;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @OneToOne
    @JoinColumn(name = "registration_id", nullable = false, unique = true)
    private Registration registration;

    public Payment() {

    }

    public Payment(Account account, Registration registration) {
        this.account = account;
        this.registration = registration;
    }

    public UUID getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(UUID paymentId) {
        this.paymentId = paymentId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Registration getRegistration() {
        return registration;
    }

    public void setRegistration(Registration registration) {
        this.registration = registration;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
