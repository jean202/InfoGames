package com.infogames.domain.file.controller;

import com.infogames.domain.file.dto.FileResponse;
import com.infogames.domain.file.service.FileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    /**
     * 파일 업로드
     */
    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("postId") Long postId,
                                       @RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "order", defaultValue = "0") int order,
                                       HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다"));
        }

        try {
            FileResponse fileResponse = fileService.uploadFile(postId, file, order);
            return ResponseEntity.ok(fileResponse);
        } catch (IOException e) {
            log.error("파일 업로드 실패", e);
            return ResponseEntity.badRequest().body(Map.of("error", "파일 업로드에 실패했습니다"));
        }
    }

    /**
     * 게시글의 파일 목록 조회
     */
    @GetMapping("/post/{postId}")
    public ResponseEntity<?> getFilesByPost(@PathVariable Long postId) {
        List<FileResponse> files = fileService.getFilesByPost(postId);
        return ResponseEntity.ok(files);
    }

    /**
     * 파일 다운로드
     */
    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long fileId) {
        try {
            FileResponse fileResponse = fileService.getFile(fileId);
            Path filePath = fileService.getFilePath(fileId);
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String encodedFileName = URLEncoder.encode(fileResponse.getOriginalName(), StandardCharsets.UTF_8)
                    .replaceAll("\\+", "%20");

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                           "attachment; filename*=UTF-8''" + encodedFileName)
                    .body(resource);
        } catch (Exception e) {
            log.error("파일 다운로드 실패", e);
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 파일 삭제
     */
    @DeleteMapping("/{fileId}")
    public ResponseEntity<?> deleteFile(@PathVariable Long fileId,
                                       HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of("error", "로그인이 필요합니다"));
        }

        try {
            fileService.deleteFile(fileId);
            return ResponseEntity.ok(Map.of("message", "파일이 삭제되었습니다"));
        } catch (IOException e) {
            log.error("파일 삭제 실패", e);
            return ResponseEntity.badRequest().body(Map.of("error", "파일 삭제에 실패했습니다"));
        }
    }
}
