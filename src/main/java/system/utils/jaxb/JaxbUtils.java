package system.utils.jaxb;

import history.generated.ObjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import system.entity.Transaction;
import system.entity.User;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.StringWriter;
import java.util.GregorianCalendar;
import java.util.List;

public final class JaxbUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(system.ApplicationWar.class);

    public static String marshalToXml(User user, List<Transaction> transactionList) {
        try {
            //TODO маршал одного объекта переделать, создается userTrans - Trans- User
            JAXBContext jaxbContext = JAXBContext.newInstance();
            Marshaller marshaller = jaxbContext.createMarshaller();
            StringWriter stringWriter = new StringWriter();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            ObjectFactory objectFactory = new ObjectFactory();
            history.generated.User userGenerated = generateUserForXml(user, objectFactory);
            marshaller.marshal(userGenerated, stringWriter);

            for (Transaction transaction : transactionList) {
                history.generated.Transaction transactionGenerated = generateTransactionForXml(objectFactory, transaction);
                marshaller.marshal(transactionGenerated,stringWriter);
            }
            return stringWriter.toString();
        } catch (JAXBException e) {
            LOGGER.error("Error during marshalling to XML.", e);
            return "Error during export data to XML.";
        }
    }

    private static history.generated.Account generateAccountForXml(ObjectFactory objectFactory, Transaction transaction, boolean isSender) {
        history.generated.Account accountGenerated = objectFactory.createAccount();
        if (isSender) {
            accountGenerated.setAccountNumber(transaction.getSender().getAccountNumber());
            accountGenerated.setUser(generateUserForXml(transaction.getSender().getUser(), objectFactory));
        }else {
            accountGenerated.setAccountNumber(transaction.getReciever().getAccountNumber());
            accountGenerated.setUser(generateUserForXml(transaction.getReciever().getUser(), objectFactory));
        }
        return accountGenerated;
    }

    private static history.generated.Transaction generateTransactionForXml(ObjectFactory objectFactory, Transaction transaction) {
        history.generated.Transaction transactionGenerated = objectFactory.createTransaction();
        transactionGenerated.setAmount(transaction.getAmount());
        transactionGenerated.setDate(dateToXmlGregorianDate(transaction));
        transactionGenerated.setDescription(transaction.getDescription());
        transactionGenerated.setStatus(transaction.getStatus());
        transactionGenerated.setType(transaction.getType());
        transactionGenerated.setSender(generateAccountForXml(objectFactory,transaction,true));
        transactionGenerated.setReciever(generateAccountForXml(objectFactory,transaction,false));
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
            GregorianCalendar gregorianDate = new GregorianCalendar();
            gregorianDate.setTime(transaction.getDate());
            xmlGregorianCalendarDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianDate);
        } catch (DatatypeConfigurationException e) {
            LOGGER.error("Error during conversion from date to xml date");
        }
        return xmlGregorianCalendarDate;
    }


}
