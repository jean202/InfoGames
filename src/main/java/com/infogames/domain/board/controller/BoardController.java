package com.infogames.domain.board.controller;

import com.infogames.domain.board.dto.PostListResponse;
import com.infogames.domain.board.dto.PostResponse;
import com.infogames.domain.board.entity.BoardType;
import com.infogames.domain.board.service.PostService;
import com.infogames.domain.file.service.FileService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final PostService postService;
    private final FileService fileService;

    /**
     * 게시판 목록
     */
    @GetMapping("/{boardType}/list")
    public String list(@PathVariable String boardType,
                      @RequestParam(defaultValue = "0") int page,
                      @RequestParam(defaultValue = "10") int size,
                      @RequestParam(required = false) String keyword,
                      Model model) {
        
        BoardType type = BoardType.valueOf(boardType.toUpperCase());
        Pageable pageable = PageRequest.of(page, size);
        
        Page<PostListResponse> posts;
        if (keyword != null && !keyword.isEmpty()) {
            posts = postService.searchPosts(type, keyword, pageable);
            model.addAttribute("keyword", keyword);
        } else {
            posts = postService.getPostList(type, pageable);
        }
        
        model.addAttribute("boardType", type);
        model.addAttribute("posts", posts);
        model.addAttribute("currentPage", page);
        
        return "board/list";
    }

    /**
     * 게시글 상세보기
     */
    @GetMapping("/{boardType}/view/{postId}")
    public String view(@PathVariable String boardType,
                      @PathVariable Long postId,
                      Model model,
                      HttpSession session) {
        
        BoardType type = BoardType.valueOf(boardType.toUpperCase());
        PostResponse post = postService.getPost(postId);
        
        // 파일 목록
        model.addAttribute("files", fileService.getFilesByPost(postId));
        
        // 수정/삭제 권한 체크
        Long userId = (Long) session.getAttribute("userId");
        boolean isAuthor = userId != null && userId.equals(post.getAuthorId());
        
        model.addAttribute("boardType", type);
        model.addAttribute("post", post);
        model.addAttribute("isAuthor", isAuthor);
        
        return "board/view";
    }

    /**
     * 게시글 작성 페이지
     */
    @GetMapping("/{boardType}/write")
    public String writeForm(@PathVariable String boardType,
                           HttpSession session,
                           Model model) {
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }
        
        BoardType type = BoardType.valueOf(boardType.toUpperCase());
        model.addAttribute("boardType", type);
        
        return "board/write";
    }

    /**
     * 게시글 수정 페이지
     */
    @GetMapping("/{boardType}/edit/{postId}")
    public String editForm(@PathVariable String boardType,
                          @PathVariable Long postId,
                          HttpSession session,
                          Model model) {
        
        Long userId = (Long) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/user/login";
        }
        
        try {
            BoardType type = BoardType.valueOf(boardType.toUpperCase());
            PostResponse post = postService.getPostForEdit(postId, userId);
            
            model.addAttribute("boardType", type);
            model.addAttribute("post", post);
            model.addAttribute("files", fileService.getFilesByPost(postId));
            
            return "board/edit";
        } catch (IllegalArgumentException e) {
            return "redirect:/board/" + boardType + "/list";
        }
    }
}
