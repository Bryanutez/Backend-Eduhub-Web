package com.eduhubpro.eduhubpro.Entity.Payment.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PaymentDto {

    @NotBlank(groups = {Modify.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo.")
    private String paymentId;

    @NotBlank(groups = {Register.class, Modify.class}, message = "La cuenta no puede ser nula.")
    private String accountId;

    @NotBlank(groups = {Register.class, Modify.class}, message = "El inscripción no puede ser nula.")
    private String registrationId;

    @NotNull(groups = {ChangeStatus.class}, message = "El estado no puede ser nulo.")
    private PaymentStatus status;

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }

    public interface Register {
    }

    public interface Modify {
    }

    public interface ChangeStatus {
    }

    public interface Consult {
    }
}