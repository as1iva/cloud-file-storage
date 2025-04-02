package org.as1iva.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PathUtil {

    public String getUserPath(String path, Long userId) {

        String defaultFolder = String.format("user-%d-files/", userId);

        return defaultFolder + path;
    }

    public String getUserPath(Long userId) {
        return String.format("user-%d-files/", userId);
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

    public String getDownloadName(String path) {

        if (isDirectory(path)) {
            return getDirectoryName(path) + ".zip";
        }

        return getFileName(path);
    }

    public String trimUserPath(String path) {
        int index = path.indexOf("/");

        if (index != -1) {
            return path.substring(index + 1);
        } else {
            return "";
        }
    }

    public boolean isDirectory(String path) {
        return path.endsWith("/");
    }
}
