package org.as1iva.service;

import io.minio.Result;
import io.minio.messages.Item;
import lombok.RequiredArgsConstructor;
import org.as1iva.dto.response.ResourceResponseDto;
import org.as1iva.exception.DataExistsException;
import org.as1iva.exception.DataNotFoundException;
import org.as1iva.exception.InternalServerException;
import org.as1iva.util.PathUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class DirectoryService {

    private final MinioService minioService;

    private final String DIRECTORY_TYPE = "DIRECTORY";
    private final String FILE_TYPE = "FILE";


    public List<ResourceResponseDto> getInfo(String path, Long userId) {
        String completePath = PathUtil.getUserPath(path, userId);

        if (!path.isEmpty() && !minioService.doesResourceExist(completePath)) {
            throw new DataNotFoundException("Directory does not exist");
        }

        Iterable<Result<Item>> objects = minioService.getObjects(completePath, false);
        List<ResourceResponseDto> resources = new ArrayList<>();

        for (Result<Item> object : objects) {
            try {
                Item item = object.get();

                if (item.objectName().equals(completePath)) {
                    continue;
                }

                String objectName = item.objectName();
                Long size = item.size();
                String type = FILE_TYPE;
                String name = PathUtil.getFileName(objectName);

                if (item.isDir()) {
                    size = null;
                    type = DIRECTORY_TYPE;
                    name = PathUtil.getDirectoryName(objectName);
                }

                ResourceResponseDto resource = ResourceResponseDto.builder()
                        .path(path)
                        .name(name)
                        .size(size)
                        .type(type)
                        .build();

                resources.add(resource);
            } catch (Exception e) {
                throw new InternalServerException();
            }
        }

        return resources;
    }


    public ResourceResponseDto createDirectory(String path, Long userId) {
        String completePath = PathUtil.getUserPath(path, userId);
        String parentPath = PathUtil.getDirectoryPath(path);
        String completeParentPath = PathUtil.getUserPath(parentPath, userId);

        if (!parentPath.isEmpty() && !minioService.doesResourceExist(completeParentPath)) {
            throw new DataNotFoundException("Parent directory does not exist");
        }

        if (minioService.doesResourceExist(completePath)) {
            throw new DataExistsException("Directory already exists");
        }

        try {
            minioService.createEmptyDirectory(completePath);
        } catch (Exception e) {
            throw new InternalServerException();
        }

        return ResourceResponseDto.builder()
                .path(PathUtil.getDirectoryPath(path))
                .name(PathUtil.getDirectoryName(path))
                .type(DIRECTORY_TYPE)
                .build();

    }

    public void createUserDirectory(Long userId) {
        String userDirectoryName = PathUtil.getUserPath(userId);

        if (!minioService.doesResourceExist(userDirectoryName)) {
            try {
                minioService.createEmptyDirectory(userDirectoryName);
            } catch (Exception e) {
                throw new InternalServerException();
            }
        }
    }
}
