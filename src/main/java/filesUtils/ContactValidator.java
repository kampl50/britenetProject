package filesUtils;

import enums.ContactType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContactValidator {

    private static final String regex = "^(.+)@(.+)$";

    public static String getType(String contact) {
        if (isValidEmail(contact)) {
            return ContactType.EMAIL.toString();
        }
        if (isValidPhoneNumber(contact)) {
            return ContactType.PHONE.toString();
        }
        if (isValidJabber(contact)) {
            return ContactType.JABBER.toString();
        }
        if (isUnknownType(contact)) {
            return ContactType.UNKNOWN.toString();
        }
        return ContactType.UNKNOWN.toString();
    }

    private static boolean isValidEmail(String contact) {
        if (!contact.contains("@"))
            return false;
        else {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(contact);

            return matcher.matches();
        }
    }

    private static boolean isValidJabber(String contact) {
        try {
            String prefixJabber = contact.substring(0, 4);
            return prefixJabber.equals("jbr:");
        } catch (StringIndexOutOfBoundsException e) {
            return false;
        }
    }

    private static boolean isValidPhoneNumber(String contact) {
        if (contact.length() < 9)
            return false;
        else {
            Pattern pattern = Pattern.compile("^(\\d{3}[- ]?){2}\\d{3}$");
            Matcher matcher = pattern.matcher(contact);

            return matcher.matches();
        }
    }

    private static boolean isUnknownType(String contact) {
        return (!isValidEmail(contact) && !isValidJabber(contact) && !isValidPhoneNumber(contact));
    }
}
