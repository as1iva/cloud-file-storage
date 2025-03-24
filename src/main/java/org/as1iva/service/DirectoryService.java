package org.as1iva.service;

import lombok.RequiredArgsConstructor;
import org.as1iva.dto.response.ResourceResponseDto;
import org.as1iva.exception.InternalServerException;
import org.as1iva.util.PathUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class DirectoryService {

    private final MinioService minioService;

    private final String DIRECTORY_TYPE = "DIRECTORY";

    public ResourceResponseDto createDirectory(String path, Long userId) {
        String completePath = PathUtil.getUserPath(path, userId);

        try {
            minioService.createEmptyDirectory(completePath);

            return ResourceResponseDto.builder()
                    .path(PathUtil.getDirectoryRoot(path))
                    .name(PathUtil.getDirectoryName(path))
                    .type(DIRECTORY_TYPE)
                    .build();

        } catch (Exception e) {
            throw new InternalServerException();
        }
    }

    private boolean isDirectoryExists(String path) {
        try {
            minioService.statObject(path);

            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
