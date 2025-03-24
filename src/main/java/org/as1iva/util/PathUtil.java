package org.as1iva.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtil {

    public String getUserPath(String path, Long userId) {

        String defaultFolder = String.format("user-%d-files/", userId + 1);

        return defaultFolder + path;
    }

    public String getFileName(String path) {

        int lastIndex = path.lastIndexOf("/");

        if (lastIndex != -1) {
            return path.substring(lastIndex + 1);
        } else {
            return path;
        }
    }

    public String getFilePath(String path) {

        int lastIndex = path.lastIndexOf("/");

        if (lastIndex != -1) {
            return path.substring(0, lastIndex + 1);
        } else {
            return "";
        }
    }

    public String getDirectoryName(String path) {

        String pathWithoutLastSlash = path.replaceAll("/+$", "");

        return getFileName(pathWithoutLastSlash);
    }

    public String getDirectoryPath(String path) {

        String pathWithoutLastSlash = path.replaceAll("/+$", "");

        return getFilePath(pathWithoutLastSlash);
    }

    public boolean isDirectory(String path) {
        return path.endsWith("/");
    }
}
