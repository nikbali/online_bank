package system.rest_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
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
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/transfer/", method = RequestMethod.POST)
    public ResponseEntity<Response> transferFromOtherBank(@RequestBody Operation operation, @RequestHeader(value="x-api-key") String api_key) {

        ResponseEntity responseEntity = null;
        //здесь будем проверять api_key, пока просто от балды написал
        if(api_key.equals("qwerty"))
        {
            if(validateInputParams(operation)){

                Currency currency = Currency.valueOf(operation.getCurrency());
                Account receiverAccount = accountService.findByAccountNumber(Long.parseLong(operation.getToAccount()));
                Account senderAccount = accountService.findByAccountNumber(Long.parseLong(operation.getFromAccount()));

                if (receiverAccount != null)
                {
                    //создаем аккаунт, если раньше такого не было
                    if(senderAccount == null){
                        senderAccount = new Account();
                        senderAccount.setCurrency(currency);
                        senderAccount.setBic(Integer.parseInt(operation.getBic()));
                        senderAccount.setAccountNumber(Long.parseLong(operation.getFromAccount()));
                    }
                    if(currency == receiverAccount.getCurrency())
                    {
                        if (Bank.existBankByBic(Integer.parseInt(operation.getBic())))
                        {
                            Transaction transaction = transactionService.transferFromOtherBank(senderAccount, receiverAccount, BigDecimal.valueOf(operation.getAmount()), operation.getComment());
                            if (transaction != null) {
                                responseEntity = new ResponseEntity(new Response(0, "Success!"), HttpStatus.OK);
                            } else
                                responseEntity = new ResponseEntity(new Response(3, "Error Server transaction!"), HttpStatus.EXPECTATION_FAILED);
                        }
                        else responseEntity = new ResponseEntity(new Response(6, "Error! Currency is incorrectly specified !"), HttpStatus.EXPECTATION_FAILED);
                    }
                    else responseEntity = new ResponseEntity(new Response(5, "Error! Currency is incorrectly specified !"), HttpStatus.EXPECTATION_FAILED);
                }
                else
                {
                    responseEntity =new ResponseEntity(new Response(2,"Error! That Account Not Exist in our Bank."), HttpStatus.EXPECTATION_FAILED);
                }
            }
            else responseEntity = new ResponseEntity(new Response(4,"Error! Incorrect request params!"), HttpStatus.EXPECTATION_FAILED);
        }
        else
        {
            responseEntity = new ResponseEntity(new Response(1,"Error API_KEY"), HttpStatus.EXPECTATION_FAILED);
        }
        return responseEntity;
    }


    public  static boolean validateInputParams(Operation operation) {
        if (operation.getFromAccount() != null &&
                operation.getToAccount() != null &&
                !operation.getFromAccount().equals("") &&
                !operation.getToAccount().equals("") &&
                operation.getAmount() > 0 &&
                operation.getId() > 0) {
            try {
                Long.parseLong(operation.getFromAccount());
                Long.parseLong(operation.getToAccount());
                Integer.parseInt(operation.getBic());
                Currency.valueOf(operation.getCurrency());
                //ВСЕ ОК
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }


        } else return false;
    }
}
