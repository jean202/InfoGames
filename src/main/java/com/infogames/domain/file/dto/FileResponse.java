package com.infogames.domain.file.dto;

import com.infogames.domain.file.entity.File;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FileResponse {

    private Long id;
    private String originalName;
    private String savedName;
    private String filePath;
    private Long fileSize;
    private String contentType;
    private Integer fileOrder;
    private Boolean isImage;

    public static FileResponse from(File file) {
        return FileResponse.builder()
                .id(file.getId())
                .originalName(file.getOriginalName())
                .savedName(file.getSavedName())
                .filePath(file.getFilePath())
                .fileSize(file.getFileSize())
                .contentType(file.getContentType())
                .fileOrder(file.getFileOrder())
                .isImage(file.isImage())
                .build();
    }
}
