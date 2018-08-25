package system.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ValidationUtils {

    private ValidationUtils() {
    }

    private static final String LOGIN_PATTERN = "(^[A-Za-z0-9]*$)";
    private static final String NAME_PATTERN = "(^[A-Za-z]{1}[a-z]*$)|(^[А-Яа-я]{1}[а-я]*$)";

    /* Password_Pattern:
    (?=.*[0-9]) - строка содержит хотя бы одно число;
    (?=.*[!@#$%^&*]) - строка содержит хотя бы один спецсимвол;
    (?=.*[a-z]) - строка содержит хотя бы одну латинскую букву в нижнем регистре;
    (?=.*[A-Z]) - строка содержит хотя бы одну латинскую букву в верхнем регистре;
    [0-9a-zA-Z!@#$%^&*]{6,} - строка состоит не менее, чем из 6 вышеупомянутых символов.*/
    private static final String PASSWORD_PATTERN = "(?=.*[0-9])(?=.*[!@#$%^&*.,])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z!@#$%^&*.,]{6,}";
    private static final String PHONE_NUMBER_PATTERN = "^(\\+7|8)[(\\s-]?\\d{3}[)\\s-]?\\d{3}([\\s-]?\\d{2}){2}$";//? = Появляется 0 или 1 раз, ? краткая форма​​​​​​​​​​​​​​​​​​​​​​​​​​​​​ {0,1}.
    private static final String DOCUMENT_PATTERN = "(\\d{4})(\\s)?\\d{6}";

    /*Email Pattern
        ^( - параметр что маска начинается с начала текста
            (
        (  - этот блок отвечает за логин латиницей
            [0-9A-Za-z]{1} - 1й символ только цифра или буква
            [-0-9A-z\.]{1,} - в середине минимум один символ (буква, цифра, _, -, .) (не менее 1 символа)
            [0-9A-Za-z]{1} - последний символ только цифра или буква
        )
           | - параметр "или/или" выбирает блок "латиница" или "кирилица"
            (  - этот блок отвечает за логин кирилицей
            [0-9А-Яа-я]{1} - 1й символ только цифра или буква
            [-0-9А-я\.]{1,} - в середине минимум один символ (буква, цифра, _, -, .) (не менее 1 символа)
            [0-9А-Яа-я]{1} - последний символ только цифра или буква
        ) )
        @ - обазятельное наличие значка разделяющего логин от домена
            (
             [-0-9A-Za-z]{1,} - блок может состоять из "-", цифр и букв (не менее 1 символа)
             \. - наличие точки в конце блока
            ){1,2} - допускается от 1 до 2 блоков по вышеукащанной маске (mail. , ru.mail.)
            [-A-Za-z]{2,} - блок описывайющий домен вехнего уровня (ru, com, net, aero etc) (не менее 2 символов)
            )$ - параметр что маска заканчивается в конце текста*/
    private static final String EMAIL_PATTERN = "^((([0-9A-Za-z]{1}[-0-9A-z\\.]{1,}[0-9A-Za-z]{1})|([0-9А-Яа-я]{1}[-0-9А-я\\.]{1,}[0-9А-Яа-я]{1}))@([-A-Za-z]{1,}\\.){1,2}[-A-Za-z]{2,})$";

    public static FieldInfo isCorrectNamePattern(String fieldName, String fieldValue) {
        FieldInfo fieldInfo = validation(fieldName,fieldValue, NAME_PATTERN);
        if (!fieldInfo.isValid()) {
            fieldInfo.setDescription(String.format("Field %s must contain only latin or cyrillic characters.", fieldName));
        }
        return fieldInfo;
    }

    public static FieldInfo isCorrectPasswordPattern(String fieldName, String fieldValue) {
        FieldInfo fieldInfo = validation(fieldName,fieldValue, PASSWORD_PATTERN);
        if (!fieldInfo.isValid()) {
            fieldInfo.setDescription(String.format("Field %s must contain at least one digit, one special characters, one lower case latin character, one upper case latin character. " +
                    "Make sure that total length is not less than 6 characters. Your password length is %s.", fieldName, fieldValue.length()));
        }
        return fieldInfo;
    }

    public static FieldInfo isCorrectPhoneNumberPattern(String fieldName, String fieldValue) {
        FieldInfo fieldInfo = validation(fieldName,fieldValue, PHONE_NUMBER_PATTERN);
        if (!fieldInfo.isValid()) {
            fieldInfo.setDescription(String.format("Field %s must contain 11 digits.", fieldName));
        }
        return fieldInfo;
    }

    public static FieldInfo isCorrectDocumentPattern(String fieldName, String fieldValue) {
        FieldInfo fieldInfo = validation(fieldName,fieldValue, DOCUMENT_PATTERN);
        if (!fieldInfo.isValid()) {
            fieldInfo.setDescription(String.format("Field %s must contain 10 digits.", fieldName));
        }
        return fieldInfo;
    }

    public static FieldInfo isCorrectEmailPattern(String fieldName, String fieldValue) {
        FieldInfo fieldInfo = validation(fieldName,fieldValue,EMAIL_PATTERN);
        if (!fieldInfo.isValid()) {
            fieldInfo.setDescription(String.format("Field %s must contain latin or cyrillic characters, digits, '@' and '.'", fieldName));
        }
        return fieldInfo;
    }

    public static FieldInfo isCorrectLoginPattern(String fieldName, String fieldValue) {
        FieldInfo fieldInfo = validation(fieldName,fieldValue,LOGIN_PATTERN);
        if (!fieldInfo.isValid()) {
            fieldInfo.setDescription(String.format("Field %s can contain latin characters and digits.", fieldName));
        }
        return fieldInfo;
    }


    private static FieldInfo validation(String fieldName,String fieldValue, String pattern) {
        FieldInfo fieldInfo = new FieldInfo(fieldName, fieldValue);
        Pattern namePattern = Pattern.compile(pattern);
        Matcher matcher = namePattern.matcher(fieldValue);
        if (!matcher.matches()) {
            fieldInfo.setValid(false);
        }else {
            fieldInfo.setValid(true);
        }
        return fieldInfo;
    }

}
