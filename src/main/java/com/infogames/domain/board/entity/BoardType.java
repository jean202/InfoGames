package com.infogames.domain.board.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BoardType {
    FREE("자유게시판", "FREE"),
    TIP("팁게시판", "TIP"),
    REVIEW("리뷰게시판", "REVIEW");

    private final String displayName;
    private final String code;

    public static BoardType fromCode(String code) {
        for (BoardType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid board type code: " + code);
    }
}
