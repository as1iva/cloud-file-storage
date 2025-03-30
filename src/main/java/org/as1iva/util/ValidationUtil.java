package org.as1iva.util;

import lombok.experimental.UtilityClass;
import org.as1iva.exception.InvalidDataException;

import java.util.regex.Pattern;

@UtilityClass
public class ValidationUtil {

    public void checkUsername(String username) {
        if (username.length() <= 4) {
            throw new InvalidDataException("Username must be at least 5 characters long");
        }
    }

    public void checkPath(String path) {
        if (path == null) {
            throw new InvalidDataException("Path is empty");
        }

        Pattern pattern = Pattern.compile(
                "^([^/]+/)*[^/]+/?$"
        );

        if (!pattern.matcher(path).matches() && !path.isEmpty()) {
            throw new InvalidDataException("Path is invalid");
        }
    }
}
