package com.infogames.global.controller;

import com.infogames.domain.board.entity.BoardType;
import com.infogames.domain.board.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        // 각 게시판의 게시글 개수
        model.addAttribute("freeCount", postService.getPostCount(BoardType.FREE));
        model.addAttribute("tipCount", postService.getPostCount(BoardType.TIP));
        model.addAttribute("reviewCount", postService.getPostCount(BoardType.REVIEW));
        
        return "index";
    }

    @GetMapping("/health")
    @ResponseBody
    public String health() {
        return "OK";
    }
}
