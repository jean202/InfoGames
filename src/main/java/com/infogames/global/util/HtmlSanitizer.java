package com.infogames.global.util;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;
import org.springframework.stereotype.Component;

/**
 * 에디터(CKEditor) 본문 HTML 을 안전하게 정제한다.
 * 서식/목록/표/링크/이미지는 허용하되 script 등 위험 요소는 제거하여 저장형 XSS 를 방지한다.
 */
@Component
public class HtmlSanitizer {

    // 인라인 이미지(width/height/style/alt) 허용
    private static final PolicyFactory IMG_POLICY = new HtmlPolicyBuilder()
            .allowElements("img")
            .allowAttributes("src", "alt", "title", "width", "height", "style").onElements("img")
            .allowUrlProtocols("http", "https")
            .allowStandardUrlProtocols()
            .toFactory();

    private static final PolicyFactory POLICY = Sanitizers.FORMATTING
            .and(Sanitizers.BLOCKS)
            .and(Sanitizers.LINKS)
            .and(Sanitizers.TABLES)
            .and(Sanitizers.STYLES)
            .and(IMG_POLICY);

    public String sanitize(String html) {
        if (html == null) {
            return null;
        }
        return POLICY.sanitize(html);
    }
}
