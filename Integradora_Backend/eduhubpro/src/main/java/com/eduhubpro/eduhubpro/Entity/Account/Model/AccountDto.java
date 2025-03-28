package com.eduhubpro.eduhubpro.Entity.Account.Model;

import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AccountDto {

    @NotNull(groups = {Modify.class, ChangeStatus.class, Consult.class}, message = "El id no puede ser nulo.")
    private String accountId;

    @NotBlank(groups = {Register.class}, message = "El titular de la cuenta no puede ser nulo.")
    private String adminId; // todo || Se obtiene de la sesión

    @NotBlank(groups = {Modify.class, Register.class}, message = "El número de cuenta no puede ser nulo.")
    @Size(groups = {Modify.class, Register.class}, max = 255, message = "El número de cuenta no puede tener más de 255 caracteres.")
    private String accountNumber;

    @NotBlank(groups = {Modify.class, Register.class}, message = "El nombre del banco no puede ser nulo.")
    @Size(groups = {Modify.class, Register.class}, max = 50, message = "El nombre del banco no puede tener más de 50 caracteres.")
    private String bankName;

    @NotBlank(groups = {Modify.class, Register.class}, message = "La clave no puede ser nula.")
    @Size(groups = {Modify.class, Register.class}, max = 255, message = "La clave no puede tener más de 255 caracteres.")
    private String key;

    @NotNull(groups = {ChangeStatus.class}, message = "El estado de la cuenta no puede ser nulo.")
    private Status status;

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
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
