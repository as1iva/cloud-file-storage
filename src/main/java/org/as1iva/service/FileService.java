package org.as1iva.service;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.as1iva.dto.response.ResourceResponseDto;
import org.as1iva.exception.DataExistsException;
import org.as1iva.exception.DataNotFoundException;
import org.as1iva.exception.InternalServerException;
import org.as1iva.util.PathUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.*;
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

        path = PathUtil.getDirectoryName(path) + "/";

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

    public ResourceResponseDto move(String oldPath, String newPath, Long userId) {
        String oldCompletePath = PathUtil.getUserPath(oldPath, userId);
        String newCompletePath = PathUtil.getUserPath(newPath, userId);

        if (!minioService.doesResourceExist(oldCompletePath)) {
            throw new DataNotFoundException("Resource not found");
        }

        if (minioService.doesResourceExist(newCompletePath)) {
            throw new DataExistsException("Resource already exists");
        }

        try (InputStream inputStream = minioService.getObject(oldCompletePath)) {
            if (PathUtil.isDirectory(oldPath)) {
                Iterable<Result<Item>> objects = minioService.getObjects(oldCompletePath, true);

                for (Result<Item> object : objects) {
                    String oldObjectName = object.get().objectName();
                    String newObjectName = newCompletePath + oldObjectName.substring(oldCompletePath.length());

                    minioService.copy(oldObjectName, newObjectName);
                }

                delete(oldPath, userId);

                return ResourceResponseDto.builder()
                        .path(PathUtil.getDirectoryPath(newPath))
                        .name(PathUtil.getDirectoryName(newPath))
                        .type(DIRECTORY_TYPE)
                        .build();
            } else {
                minioService.copy(oldCompletePath, newCompletePath);

                delete(oldPath, userId);

                return ResourceResponseDto.builder()
                        .path(PathUtil.getFilePath(newPath))
                        .name(PathUtil.getFileName(newPath))
                        .size((long) inputStream.readAllBytes().length)
                        .type(FILE_TYPE)
                        .build();
            }
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    public List<ResourceResponseDto> search(String query,Long userId) {
        String userPath = PathUtil.getUserPath(userId);

        Iterable<Result<Item>> objects = minioService.getObjects(userPath, true);

        List<ResourceResponseDto> resources = new ArrayList<>();

        try {
            for (Result<Item> object : objects) {
                String objectName = object.get().objectName();

                String searchName;

                if (PathUtil.isDirectory(objectName)) {
                    searchName = PathUtil.getDirectoryName(objectName);
                } else {
                    searchName = PathUtil.getFileName(objectName);
                }

                if (searchName.toLowerCase().contains(query.toLowerCase()) && !query.isEmpty()) {
                    ResourceResponseDto resource;

                    if (PathUtil.isDirectory(objectName)) {
                        resource = ResourceResponseDto.builder()
                                .path(PathUtil.getDirectoryPath(PathUtil.trimUserPath(objectName)))
                                .name(PathUtil.getDirectoryName(objectName) + "/")
                                .type(DIRECTORY_TYPE)
                                .build();

                    } else {
                        resource = ResourceResponseDto.builder()
                                .path(PathUtil.getFilePath(PathUtil.trimUserPath(objectName)))
                                .name(PathUtil.getFileName(objectName))
                                .size(object.get().size())
                                .type(FILE_TYPE)
                                .build();
                    }

                    resources.add(resource);
                }
            }
        } catch (Exception e) {
            throw new InternalServerException();
        }

        return resources;
    }

    public List<ResourceResponseDto> upload(List<MultipartFile> files, String path, Long userId) {
        String completePath = PathUtil.getUserPath(path, userId);

        List<ResourceResponseDto> resources = new ArrayList<>();

        if (files.size() > 1) {
            createMissingDirectories(files, completePath);
        }

        for (MultipartFile file : files) {
            String directoryName = PathUtil.getFilePath(file.getOriginalFilename());

            if (minioService.doesResourceExist(completePath + file.getOriginalFilename())) {
                throw new DataExistsException("Resource already exists");
            }

            try (InputStream input = file.getInputStream()) {

                minioService.upload(input, completePath, file);
            } catch (Exception e) {
                throw new InternalServerException();
            }

            ResourceResponseDto resource;

            if (PathUtil.isDirectory(path)) {
                resource = ResourceResponseDto.builder()
                        .path(path)
                        .name(PathUtil.getFileName(file.getOriginalFilename()))
                        .type(DIRECTORY_TYPE)
                        .build();

            } else {
                resource = ResourceResponseDto.builder()
                        .path(directoryName)
                        .name(PathUtil.getFileName(file.getOriginalFilename()))
                        .size(file.getSize())
                        .type(FILE_TYPE)
                        .build();
            }

            resources.add(resource);
        }

        return resources;
    }

    private void createMissingDirectories(List<MultipartFile> files, String completePath) {
        Set<String> directories = new HashSet<>();

        for (MultipartFile file : files) {
            String directoryName = PathUtil.getFilePath(file.getOriginalFilename());

            directories.add(completePath + directoryName);
        }

        for (String directory : directories) {
            if (minioService.doesResourceExist(directory)) {
                throw new DataExistsException("Directory already exists");
            }

            try {
                minioService.createEmptyDirectory(directory);
            } catch (Exception e) {
                throw new InternalServerException();
            }
        }
    }
}
