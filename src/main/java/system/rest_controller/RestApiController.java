package system.rest_controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import system.entity.Account;
import system.entity.Transaction;
import system.service.AccountService;
import system.service.TransactionService;

@RestController
@RequestMapping("/api")
public class RestApiController {

    @Autowired
    AccountService accountService;
    @Autowired
    TransactionService transactionService;

    @RequestMapping(value = "/transfer/", method = RequestMethod.POST)
    public ResponseEntity<Response> transferFromOtherBank(@RequestBody Operation operation, @RequestHeader(value="x-api-key") String api_key) {

        ResponseEntity responseEntity =null;
        //здесь будем проверять api_key, пока просто от балды написал
        if(api_key.equals("qwerty"))
        {

            if(operation.getFrom() != null && operation.getTo() != null && !operation.getFrom().equals("") && !operation.getTo().equals("") && operation.getAmount() > 0)
            {
                Account receiverAccount = accountService.findByAccountNumber(Long.parseLong(operation.getTo()));
                if (receiverAccount != null)
                {
                    //здесь пока непонятно что в сендере добавляем
                    Account senderAccount = new Account();
                    senderAccount.setAccountNumber(Long.parseLong(operation.getFrom()));

                    Transaction transaction = transactionService.transfer(senderAccount, receiverAccount, operation.getAmount());
                    if(transaction != null) {
                        responseEntity = new ResponseEntity(new Response(0, "Success!"), HttpStatus.OK);
                    }
                    else responseEntity = new ResponseEntity(new Response(3, "Error Server transaction!"), HttpStatus.EXPECTATION_FAILED);

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

}
