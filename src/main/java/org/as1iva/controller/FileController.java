package org.as1iva.controller;

import lombok.RequiredArgsConstructor;
import org.as1iva.dto.response.ResourceResponseDto;
import org.as1iva.security.SecurityUserDetails;
import org.as1iva.service.FileService;
import org.as1iva.util.PathUtil;
import org.as1iva.util.ValidationUtil;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/resource")
    public ResponseEntity<ResourceResponseDto> getInfo(@RequestParam("path") String path,
                                                       @AuthenticationPrincipal SecurityUserDetails userDetails) {

        ValidationUtil.checkPath(path);

        return ResponseEntity.ok().body(fileService.getInfo(path, userDetails.getId()));
    }

    @DeleteMapping("/resource")
    public ResponseEntity<Void> delete(@RequestParam("path") String path,
                       @AuthenticationPrincipal SecurityUserDetails userDetails) {

        ValidationUtil.checkPath(path);

        fileService.delete(path, userDetails.getId());

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/resource/download")
    public ResponseEntity<Resource> download(@RequestParam("path") String path,
                                             @AuthenticationPrincipal SecurityUserDetails userDetails) {

        ValidationUtil.checkPath(path);

        InputStreamResource object = fileService.download(path, userDetails.getId());

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(PathUtil.getDownloadName(path), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .body(object);
    }

    @PostMapping("/resource")
    public ResponseEntity<List<ResourceResponseDto>> upload(@RequestParam("path") String path,
                                                            @AuthenticationPrincipal SecurityUserDetails userDetails,
                                                            @RequestPart("object") List<MultipartFile> file) {

        return ResponseEntity.status(HttpStatus.CREATED).body(fileService.upload(file, path, userDetails.getId()));
    }
}
