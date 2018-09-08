package system.utils.jaxb;

import history.generated.ObjectFactory;
import history.generated.TransactionsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import system.entity.Transaction;
import system.entity.User;

import javax.servlet.ServletOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.OutputStream;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public final class JaxbUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(system.ApplicationWar.class);

    public static String marshalToXml(List<Transaction> transactionList) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("history.generated");
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter stringWriter = new StringWriter();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            ObjectFactory objectFactory = new ObjectFactory();
            TransactionsList generatedTransactionList = objectFactory.createTransactionsList();

            for (Transaction transaction : transactionList) {
                generatedTransactionList.getTransaction().add(generateTransactionForXml(objectFactory, transaction));
            }
            marshaller.marshal(generatedTransactionList, stringWriter);
            return stringWriter.toString();
        } catch (JAXBException e) {
            LOGGER.error("Error during marshalling to XML.", e);
            throw new IllegalStateException(e);
        }
    }

    private static history.generated.Account generateAccountForXml(ObjectFactory objectFactory, Transaction transaction, boolean isSender) {
        history.generated.Account accountGenerated = objectFactory.createAccount();
        if (isSender) {
            accountGenerated.setAccountNumber(transaction.getSender().getAccountNumber());
            accountGenerated.setUser(generateUserForXml(transaction.getSender().getUser(), objectFactory));
        } else {
            accountGenerated.setAccountNumber(transaction.getReciever().getAccountNumber());
            accountGenerated.setUser(generateUserForXml(transaction.getReciever().getUser(), objectFactory));
        }
        return accountGenerated;
    }

    private static history.generated.Transaction generateTransactionForXml(ObjectFactory objectFactory, Transaction transaction) {
        history.generated.Transaction transactionGenerated = objectFactory.createTransaction();
        transactionGenerated.setAmount(transaction.getAmount().doubleValue());
        transactionGenerated.setDate(dateToXmlGregorianDate(transaction));
        transactionGenerated.setDescription(transaction.getDescription());
        transactionGenerated.setStatus(transaction.getStatus().getName());
        transactionGenerated.setType(transaction.getType().getName());
        transactionGenerated.setSender(generateAccountForXml(objectFactory, transaction, true));
        transactionGenerated.setReciever(generateAccountForXml(objectFactory, transaction, false));
        return transactionGenerated;
    }

    private static history.generated.User generateUserForXml(User user, ObjectFactory objectFactory) {
        history.generated.User userGenerated = objectFactory.createUser();

        userGenerated.setFirstName(user.getFirst_name());
        userGenerated.setLastName(user.getLast_name());
        userGenerated.setMiddleName(user.getMiddle_name());
        return userGenerated;
    }

    private static XMLGregorianCalendar dateToXmlGregorianDate(Transaction transaction) {
        XMLGregorianCalendar xmlGregorianCalendarDate = null;
        try {
            String formatter = "yyyy-MM-dd'T'HH:mm:ss";
            xmlGregorianCalendarDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(new SimpleDateFormat(formatter).format(Date.from(transaction.getDate())));
        } catch (DatatypeConfigurationException e) {
            LOGGER.error("Error during conversion from date to xml date");
        }
        return xmlGregorianCalendarDate;
    }
}
