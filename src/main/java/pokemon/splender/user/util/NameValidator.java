package pokemon.splender.user.util;

import java.util.regex.Pattern;
import pokemon.splender.exception.CustomMVCException;

public class NameValidator {

    private static final Pattern ONLY_NUMBER = Pattern.compile("^[0-9]+$");
    private static final Pattern EMOJI = Pattern.compile("[\\uD83C-\\uDBFF\\uDC00-\\uDFFF]");
    private static final Pattern VALID_CHARACTERS = Pattern.compile("^[a-zA-Z0-9가-힣]+$");

    public static void validate(String name) {
        if (name == null || name.isBlank()) {
            throw CustomMVCException.nameRequired();
        }

        if (name.length() < 2 || name.length() > 8) {
            throw CustomMVCException.invalidNameLength();
        }

        if (ONLY_NUMBER.matcher(name).matches()) {
            throw CustomMVCException.invalidNameFormatOnlyNumber();
        }

        if (EMOJI.matcher(name).find()) {
            throw CustomMVCException.invalidNameFormatNotEmoticon();
        }

        if (!VALID_CHARACTERS.matcher(name).matches()) {
            throw CustomMVCException.invalidNameFormat();
        }
    }
}
