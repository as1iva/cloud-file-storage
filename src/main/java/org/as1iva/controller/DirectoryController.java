package org.as1iva.controller;

import lombok.RequiredArgsConstructor;
import org.as1iva.dto.response.ResourceResponseDto;
import org.as1iva.security.SecurityUserDetails;
import org.as1iva.service.DirectoryService;
import org.as1iva.util.ValidationUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DirectoryController {

    private final DirectoryService directoryService;

    @PostMapping("/directory")
    public ResponseEntity<ResourceResponseDto> createEmptyDirectory(@RequestParam("path") String path,
                                                                    @AuthenticationPrincipal SecurityUserDetails userDetails) {

        ValidationUtil.checkPath(path);

        return ResponseEntity.status(HttpStatus.CREATED).body(directoryService.createDirectory(path, userDetails.getId()));
    }
}
