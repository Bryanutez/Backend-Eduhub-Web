package com.eduhubpro.eduhubpro.Entity.Account.Model;

import com.eduhubpro.eduhubpro.Entity.Payment.Model.Payment;
import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "account")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID accountId;

    @Column(name = "account_number", columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
    private String accountNumber;

    @Column(name = "`key`", columnDefinition = "VARCHAR(255)", nullable = false, unique = true)
    private String key;

    @Column(name = "bank_name", columnDefinition = "VARCHAR(50)", nullable = false)
    private String bankName;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.ACTIVE;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<Payment> payments;

    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false)
    private User admin; // Una cuenta le pertenece a un usuario con rol admin

    public Account() {

    }

    public Account(User admin, String bankName, String accountNumber, String key) {
        this.admin = admin;
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.key = key;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public User getAdmin() {
        return admin;
    }

    public void setAdmin(User admin) {
        this.admin = admin;
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
}
