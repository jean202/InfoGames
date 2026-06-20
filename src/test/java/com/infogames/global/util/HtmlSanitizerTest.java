package com.infogames.global.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class HtmlSanitizerTest {

    private final HtmlSanitizer sanitizer = new HtmlSanitizer();

    @Test
    @DisplayName("script 등 위험 태그는 제거된다")
    void removesScript() {
        String dirty = "<p>안녕</p><script>alert('xss')</script>";
        String clean = sanitizer.sanitize(dirty);
        assertThat(clean).contains("안녕");
        assertThat(clean).doesNotContain("<script");
        assertThat(clean).doesNotContain("alert");
    }

    @Test
    @DisplayName("서식과 인라인 이미지는 유지된다")
    void keepsFormattingAndImage() {
        String html = "<p><strong>굵게</strong></p><img src=\"/uploads/images/a.png\" alt=\"img\">";
        String clean = sanitizer.sanitize(html);
        assertThat(clean).contains("<strong>");
        assertThat(clean).contains("<img");
        assertThat(clean).contains("/uploads/images/a.png");
    }

    @Test
    @DisplayName("이벤트 핸들러 속성은 제거된다")
    void removesEventHandlers() {
        String html = "<img src=\"/uploads/images/a.png\" onerror=\"alert(1)\">";
        String clean = sanitizer.sanitize(html);
        assertThat(clean).doesNotContain("onerror");
    }
}
