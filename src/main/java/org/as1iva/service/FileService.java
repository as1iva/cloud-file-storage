package org.as1iva.service;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.as1iva.dto.response.ResourceResponseDto;
import org.as1iva.exception.DataNotFoundException;
import org.as1iva.exception.InternalServerException;
import org.as1iva.util.PathUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final MinioService minioService;
    
    private final String DIRECTORY_TYPE = "DIRECTORY";

    private final String FILE_TYPE = "FILE";

    public ResourceResponseDto getInfo(String path, Long userId) {
        String completePath = PathUtil.getUserPath(path, userId);

        if (!minioService.doesResourceExist(completePath)) {
            throw new DataNotFoundException("Resource not found");
        }

        try (InputStream object = minioService.getObject(completePath)) {
            if (PathUtil.isDirectory(completePath)) {
                return ResourceResponseDto.builder()
                        .path(PathUtil.getDirectoryPath(path))
                        .name(PathUtil.getDirectoryName(path))
                        .type(DIRECTORY_TYPE)
                        .build();
            }

            return ResourceResponseDto.builder()
                    .path(PathUtil.getFilePath(path))
                    .name(PathUtil.getFileName(path))
                    .size((long) object.readAllBytes().length)
                    .type(FILE_TYPE)
                    .build();

        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    public void delete(String path, Long userId) {
        String completePath = PathUtil.getUserPath(path, userId);

        if (!minioService.doesResourceExist(completePath)) {
            throw new DataNotFoundException("Resource not found");
        }

        try {
            if (PathUtil.isDirectory(path)) {
                Iterable<Result<Item>> objects = minioService.getObjects(completePath, true);

                for (Result<Item> object : objects) {
                    String objectName = object.get().objectName();

                    minioService.removeObject(objectName);
                }
            } else {
                minioService.removeObject(completePath);
            }
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    public InputStreamResource download(String path, Long userId) {
        String completePath = PathUtil.getUserPath(path, userId);

        if (!minioService.doesResourceExist(completePath)) {
            throw new DataNotFoundException("Resource not found");
        }

        if (PathUtil.isDirectory(path)) {
            return downloadDirectory(completePath, path);
        }

        return downloadFile(completePath);
    }

    private InputStreamResource downloadDirectory(String completePath, String path) {
        ByteArrayOutputStream zipBuffer = new ByteArrayOutputStream();

        try (ZipOutputStream zipOutputStream = new ZipOutputStream(zipBuffer)) {
            Iterable<Result<Item>> objects = minioService.getObjects(completePath, true);

            for (Result<Item> object : objects) {
                Item item = object.get();

                if (item.objectName().equals(completePath)) {
                    continue;
                }

                InputStream input = minioService.getObject(item.objectName());

                String resourcePath = path + item.objectName().substring(completePath.length());

                zipOutputStream.putNextEntry(new ZipEntry(resourcePath));
                input.transferTo(zipOutputStream);
                zipOutputStream.closeEntry();

            }
        } catch (Exception e) {
            throw new InternalServerException();
        }

        ByteArrayInputStream zipContent = new ByteArrayInputStream(zipBuffer.toByteArray());

        return new InputStreamResource(zipContent);
    }

    private InputStreamResource downloadFile(String completePath) {
        try {
            InputStream object = minioService.getObject(completePath);

            return new InputStreamResource(object);
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
