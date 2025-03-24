package org.as1iva.controller;

import lombok.RequiredArgsConstructor;
import org.as1iva.dto.response.ResourceResponseDto;
import org.as1iva.security.SecurityUserDetails;
import org.as1iva.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/resource")
    public ResponseEntity<ResourceResponseDto> getInfo(@RequestParam("path") String path,
                                                       @AuthenticationPrincipal SecurityUserDetails userDetails) {

        return ResponseEntity.ok().body(fileService.getInfo(path, userDetails.getId()));
    }
}
