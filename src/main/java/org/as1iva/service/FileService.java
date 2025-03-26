package org.as1iva.service;

import io.minio.errors.ErrorResponseException;
import lombok.RequiredArgsConstructor;
import org.as1iva.dto.response.ResourceResponseDto;
import org.as1iva.exception.DataNotFoundException;
import org.as1iva.exception.InternalServerException;
import org.as1iva.util.PathUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
@Transactional
@RequiredArgsConstructor
public class FileService {

    private final MinioService minioService;
    
    private final String DIRECTORY_TYPE = "DIRECTORY";

    private final String FILE_TYPE = "FILE";

    public ResourceResponseDto getInfo(String path, Long userId) {
        String completePath = PathUtil.getUserPath(path, userId);

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

        }  catch (ErrorResponseException e) {
            throw new DataNotFoundException("Resource not found");
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
