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
     * CKEditor 인라인 이미지 업로드 (레거시 ImgFileUpload 대체).
     * CKEditor 4 의 filebrowserUploadUrl 규약에 맞춰 {uploaded, fileName, url} 을 반환한다.
     */
    @PostMapping("/image")
    public ResponseEntity<?> uploadImage(@RequestParam("upload") MultipartFile upload,
                                         HttpSession session) {
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return ResponseEntity.status(401).body(Map.of(
                    "uploaded", 0,
                    "error", Map.of("message", "로그인이 필요합니다")));
        }
        try {
            String url = fileService.uploadImage(upload);
            return ResponseEntity.ok(Map.of(
                    "uploaded", 1,
                    "fileName", upload.getOriginalFilename() != null ? upload.getOriginalFilename() : "image",
                    "url", url));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.ok(Map.of(
                    "uploaded", 0,
                    "error", Map.of("message", e.getMessage())));
        } catch (IOException e) {
            log.error("이미지 업로드 실패", e);
            return ResponseEntity.ok(Map.of(
                    "uploaded", 0,
                    "error", Map.of("message", "이미지 업로드에 실패했습니다")));
        }
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
