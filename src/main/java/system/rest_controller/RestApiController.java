package system.rest_controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import system.entity.Account;
import system.entity.Transaction;
import system.enums.Bank;
import system.enums.Currency;
import system.service.AccountService;
import system.service.TransactionService;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    private AccountService accountService;
    @Autowired
    private TransactionService transactionService;

    private static final Logger LOGGER = LoggerFactory.getLogger(system.ApplicationWar.class);

    /**
     * Метод обрабатывает Post запрос для передачи средств со счета с внешнего банка на счет в наш банк
     * @param transactionDTO Тело запроса содержит информацию о переводе
     * @param api_key Уникальный ключ передается в header параметре x-api-key
     * @return ответ в виде Json объекта, с результатом операции
     */
    @RequestMapping(value = "/transfer/", method = RequestMethod.POST)
    public ResponseEntity<?> transferFromOtherBank(@RequestBody TransactionDTO transactionDTO, @RequestHeader(value="x-api-key") String api_key) {

        LOGGER.info("Received GET request for transfer funds from another bank to {}", transactionDTO.getToAccount());
        ResponseEntity responseEntity = null;
        //здесь будем проверять api_key, пока просто от балды написал
        if(!api_key.equals("hello")) {
            LOGGER.error("Error API_KEY!");
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(new Response(HttpStatus.FORBIDDEN,"Error API_KEY"));
        }
        if(!validateInputParams(transactionDTO)) {
            LOGGER.error("Error! Incorrect request params!");
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error! Incorrect request params!"));

        }
        Currency currency = Currency.valueOf(transactionDTO.getCurrency());
        Account receiverAccount = accountService.findByAccountNumber(Long.parseLong(transactionDTO.getToAccount()));
        Account senderAccount = accountService.findByAccountNumber(Long.parseLong(transactionDTO.getFromAccount()));

        //здесь на всякий еще проверяем первые разряды, чтобы наверянка перевод был на нужные счета
        if (receiverAccount == null || !transactionDTO.getToAccount().substring(0,2).equals(String.valueOf(Bank.OUR_BANK.getBic()))) {
            LOGGER.error("Error! That Account Not Exist in our Bank.");
            return  ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error! That Account Not Exist in our Bank."));
        }
        //создаем аккаунт, если раньше такого не было
        if(senderAccount == null){
            senderAccount = new Account();
            senderAccount.setCurrency(currency);
            transactionDTO.getFromAccount().substring(0,2);
            senderAccount.setBic(Integer.parseInt(transactionDTO.getFromAccount().substring(0,2)));
            senderAccount.setAccountNumber(Long.parseLong(transactionDTO.getFromAccount()));
        }
        if(currency != receiverAccount.getCurrency()) {
            LOGGER.error("Error! Currency is incorrectly specified for transfer to {}", transactionDTO.getToAccount());
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error! Currency is incorrectly specified!"));
        }

        if (!Bank.existBankByBic(Integer.parseInt(transactionDTO.getFromAccount().substring(0,2)))) {
            LOGGER.error("Error! Unknown Identificator Bank for transfer to Account {}", transactionDTO.getToAccount());
            return ResponseEntity
                    .status(HttpStatus.NOT_ACCEPTABLE)
                    .body(new Response(HttpStatus.NOT_ACCEPTABLE, "Error! Unknown Identificator Bank(first two digits in your Account Number)!"));
        }
        Transaction transaction = transactionService.transferFromOtherBank(senderAccount, receiverAccount, BigDecimal.valueOf(transactionDTO.getAmount()), transactionDTO.getComment());
        if (transaction == null) {
            LOGGER.error("Error Server transaction for transfer to {}", transactionDTO.getToAccount());
            return ResponseEntity
                    .status(HttpStatus.NOT_IMPLEMENTED)
                    .body(new Response(HttpStatus.NOT_IMPLEMENTED, "Error Server transaction!"));
        }

        LOGGER.info("Success transfer to {}", transactionDTO.getToAccount());
        return ResponseEntity
            .status(HttpStatus.OK)
            .body(new Response(HttpStatus.OK, "Success!"));
    }

    /**
     * Метод обрабатывает Get запрос для проверки, существует ли счет в банке с указынным номером
     * @param account_number
     * @return ответ в виде Json объекта, с результатом
     */
    @RequestMapping(value = "/account/{account_number}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser(@PathVariable("account_number") long account_number) {
        LOGGER.info("Fetching Account with account_number {}", account_number);
        Account account = accountService.findByAccountNumber(account_number);
        if (account == null || !String.valueOf(account_number).substring(0,2).equals(String.valueOf(Bank.OUR_BANK.getBic()))) {
            LOGGER.error("Account with account_number: {} not found.", account_number);
            return new ResponseEntity(new Response(HttpStatus.NOT_FOUND,"Account with account_number: " + account_number
                    + " not found"), HttpStatus.NOT_FOUND);
        }
        return  new ResponseEntity(new Response(HttpStatus.OK, "Success! Account with account_number: " + account_number + " exist."), HttpStatus.OK);
    }

    public  static boolean validateInputParams(TransactionDTO transactionDTO) {
        if (transactionDTO.getFromAccount() != null &&
                transactionDTO.getToAccount() != null &&
                !transactionDTO.getFromAccount().equals("") &&
                !transactionDTO.getToAccount().equals("") &&
                transactionDTO.getAmount() > 0)
                 {
            try {
                Long.parseLong(transactionDTO.getFromAccount());
                Long.parseLong(transactionDTO.getToAccount());
                Currency.valueOf(transactionDTO.getCurrency());
                //ВСЕ ОК
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }


        } else return false;
    }
}
