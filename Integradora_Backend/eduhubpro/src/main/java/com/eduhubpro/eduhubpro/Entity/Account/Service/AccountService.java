package com.eduhubpro.eduhubpro.Entity.Account.Service;

import com.eduhubpro.eduhubpro.Entity.Account.Model.Account;
import com.eduhubpro.eduhubpro.Entity.Account.Model.AccountDto;
import com.eduhubpro.eduhubpro.Entity.Account.Model.AccountRepository;
import com.eduhubpro.eduhubpro.Entity.Category.Service.CategoryService;
import com.eduhubpro.eduhubpro.Entity.User.Model.User;
import com.eduhubpro.eduhubpro.Entity.User.Model.UserRepository;
import com.eduhubpro.eduhubpro.Util.Enum.EntityEnum.Status;
import com.eduhubpro.eduhubpro.Util.Enum.TypesResponse;
import com.eduhubpro.eduhubpro.Util.Response.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AccountService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;


    @Autowired
    public AccountService(AccountRepository accountRepository, UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    // --------------------------------------------
    @Transactional(readOnly = true)
    public ResponseEntity<Message> findAllActives() {
        List<Account> accounts = accountRepository.findAllByStatusActive(Status.ACTIVE);
        logger.info("Búsqueda de cuentas activas realizada correctamente");
        return new ResponseEntity<>(new Message(accounts, "Listado de cuentas activas", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> save(AccountDto dto) {
        UUID adminId = UUID.fromString(dto.getAdminId());
        Optional<User> optionalAdmin = userRepository.findById(adminId);

        if (optionalAdmin.isEmpty()) {
            return new ResponseEntity<>(new Message("No se pudo encontrar la cuenta del usuario.", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Account account = new Account(optionalAdmin.get(), dto.getBankName(), dto.getAccountNumber(), dto.getKey());

        account = accountRepository.saveAndFlush(account);

        if (account == null) {
            return new ResponseEntity<>(new Message("No se pudo registrar la cuenta bancaria.", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("El registro ha sido realizado correctamente");
        return new ResponseEntity<>(new Message(account, "Cuenta registrada correctamente bancaria.", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> update(AccountDto dto) {

        UUID uuid = UUID.fromString(dto.getAccountId());
        Optional<Account> optionalAccount = accountRepository.findById(uuid);

        if (optionalAccount.isEmpty()) {
            return new ResponseEntity<>(new Message("Cuenta bancaria no encontrada", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Account account = optionalAccount.get();

        account.setBankName(dto.getBankName());
        account.setAccountNumber(dto.getAccountNumber());
        account.setKey(dto.getKey());
        account = accountRepository.saveAndFlush(account);

        if (account == null) {
            return new ResponseEntity<>(new Message("Error al editar la cuenta bancaria.", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        logger.info("La actualización de la cuenta bancaria ha sido realizada correctamente");
        return new ResponseEntity<>(new Message(account, "Cuenta bancaria editada exitósamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }

    // --------------------------------------------
    @Transactional(rollbackFor = {SQLException.class})
    public ResponseEntity<Message> changeStatus(AccountDto dto) {
        UUID uuid = UUID.fromString(dto.getAccountId());
        Optional<Account> optionalAccount = accountRepository.findById(uuid);

        if (optionalAccount.isEmpty()) {
            return new ResponseEntity<>(new Message("No se encontró la cuenta bancaria.", TypesResponse.ERROR), HttpStatus.NOT_FOUND);
        }

        Account account = optionalAccount.get();

        account.setStatus(dto.getStatus());
        account = accountRepository.saveAndFlush(account);
        if (account == null) {
            return new ResponseEntity<>(new Message("Error al cambiar el estado de la cuenta bancaria.", TypesResponse.ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        logger.info("El estado de la cuenta bancaria ha sido actualizado correctamente");
        return new ResponseEntity<>(new Message(account, "Se cambió el estado de la cuenta bancaria correctamente", TypesResponse.SUCCESS), HttpStatus.OK);
    }
}