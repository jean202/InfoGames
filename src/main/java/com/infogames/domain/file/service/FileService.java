package com.infogames.domain.file.service;

import com.infogames.domain.board.entity.Post;
import com.infogames.domain.board.repository.PostRepository;
import com.infogames.domain.file.dto.FileResponse;
import com.infogames.domain.file.entity.File;
import com.infogames.domain.file.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FileService {

    private final FileRepository fileRepository;
    private final PostRepository postRepository;

    @Value("${file.upload.path:uploads}")
    private String uploadPath;

    /**
     * 파일 업로드
     */
    @Transactional
    public FileResponse uploadFile(Long postId, MultipartFile file, int order) throws IOException {
        log.info("파일 업로드 시도: 게시글 ID {}, 파일명 {}", postId, file.getOriginalFilename());

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        // 파일 저장
        String savedName = generateUniqueFileName(file.getOriginalFilename());
        Path filePath = Paths.get(uploadPath, savedName);

        // 디렉토리 생성
        Files.createDirectories(filePath.getParent());

        // 파일 저장
        Files.write(filePath, file.getBytes());

        // DB에 저장
        File fileEntity = File.builder()
                .post(post)
                .originalName(file.getOriginalFilename())
                .savedName(savedName)
                .filePath(uploadPath)
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .fileOrder(order)
                .build();

        File savedFile = fileRepository.save(fileEntity);
        log.info("파일 업로드 완료: ID {}", savedFile.getId());

        return FileResponse.from(savedFile);
    }

    /**
     * 게시글의 파일 목록 조회
     */
    public List<FileResponse> getFilesByPost(Long postId) {
        List<File> files = fileRepository.findByPostIdOrderByFileOrderAsc(postId);
        return files.stream()
                .map(FileResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 파일 다운로드 정보 조회
     */
    public FileResponse getFile(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일입니다"));
        return FileResponse.from(file);
    }

    /**
     * 파일 삭제
     */
    @Transactional
    public void deleteFile(Long fileId) throws IOException {
        log.info("파일 삭제 시도: ID {}", fileId);

        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일입니다"));

        // 실제 파일 삭제
        Path filePath = Paths.get(file.getFilePath(), file.getSavedName());
        Files.deleteIfExists(filePath);

        // DB에서 삭제
        fileRepository.delete(file);
        log.info("파일 삭제 완료: ID {}", fileId);
    }

    /**
     * 게시글 삭제 시 모든 파일 삭제
     */
    @Transactional
    public void deleteFilesByPost(Long postId) throws IOException {
        log.info("게시글의 모든 파일 삭제: 게시글 ID {}", postId);

        List<File> files = fileRepository.findByPostIdOrderByFileOrderAsc(postId);

        for (File file : files) {
            // 실제 파일 삭제
            Path filePath = Paths.get(file.getFilePath(), file.getSavedName());
            Files.deleteIfExists(filePath);
        }

        // DB에서 삭제
        fileRepository.deleteByPostId(postId);
        log.info("게시글의 모든 파일 삭제 완료: 게시글 ID {}", postId);
    }

    /**
     * 고유한 파일명 생성
     */
    private String generateUniqueFileName(String originalFilename) {
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex > 0) {
            extension = originalFilename.substring(dotIndex);
        }
        return UUID.randomUUID().toString() + extension;
    }

    /**
     * 파일 경로 반환
     */
    public Path getFilePath(Long fileId) {
        File file = fileRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파일입니다"));
        return Paths.get(file.getFilePath(), file.getSavedName());
    }
}
