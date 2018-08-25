package regexp;

import org.junit.Assert;
import org.junit.Test;
import system.utils.FieldInfo;
import system.utils.ValidationUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExpTest {
    @Test
    public void testName(){
        String name = "Светлана";
        String NAME_PATTERN = "(^[A-Za-z]{1}[a-z]*$)|(^[А-Яа-я]{1}[а-я]*$)";
        Pattern namePattern = Pattern.compile(NAME_PATTERN);
        Matcher matcher = namePattern.matcher(name);
        Assert.assertTrue(matcher.matches());


        FieldInfo fieldInfo = ValidationUtils.isCorrectNamePattern("c",name);
        Assert.assertTrue(fieldInfo.isValid());

    }
}

