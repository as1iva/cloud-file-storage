package org.as1iva.util;

import lombok.experimental.UtilityClass;
import org.as1iva.exception.InvalidDataException;

@UtilityClass
public class ValidationUtil {

    public void checkUsername(String username) {
        if (username.length() <= 4) {
            throw new InvalidDataException("Username must be at least 5 characters long");
        }
    }
}
