package com.infogames.domain.board.service;

import com.infogames.domain.board.dto.*;
import com.infogames.domain.board.entity.BoardType;
import com.infogames.domain.board.entity.Post;
import com.infogames.domain.board.entity.ReviewPost;
import com.infogames.domain.board.repository.PostRepository;
import com.infogames.domain.user.entity.User;
import com.infogames.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final com.infogames.global.util.HtmlSanitizer htmlSanitizer;

    /**
     * 게시글 작성
     */
    @Transactional
    public PostResponse createPost(Long userId, PostCreateRequest request) {
        log.info("게시글 작성 시도: 사용자 ID {}, 게시판 {}", userId, request.getBoardType());

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        Post post;
        String content = htmlSanitizer.sanitize(request.getContent());

        // ReviewBoard인 경우
        if (request.getBoardType() == BoardType.REVIEW) {
            if (request.getRating() == null) {
                throw new IllegalArgumentException("평점은 필수입니다");
            }

            post = ReviewPost.builder()
                    .author(user)
                    .boardType(BoardType.REVIEW)
                    .title(request.getTitle())
                    .content(content)
                    .rating(request.getRating())
                    .viewCount(0)
                    .build();

            ((ReviewPost) post).validateRating();
        } else {
            // 일반 게시글 (FREE/TIP)
            post = Post.builder()
                    .author(user)
                    .boardType(request.getBoardType())
                    .title(request.getTitle())
                    .content(content)
                    .viewCount(0)
                    .build();
        }

        Post savedPost = postRepository.save(post);
        log.info("게시글 작성 완료: ID {}", savedPost.getId());

        return PostResponse.from(savedPost);
    }

    /**
     * 게시글 목록 조회 (페이징)
     */
    public Page<PostListResponse> getPostList(BoardType boardType, Pageable pageable) {
        Page<Post> posts = postRepository.findByBoardTypeOrderByCreatedAtDesc(boardType, pageable);
        return posts.map(PostListResponse::from);
    }

    /**
     * 게시글 목록 조회 (전체 - N+1 해결)
     */
    public List<PostListResponse> getPostListWithAuthor(BoardType boardType) {
        List<Post> posts = postRepository.findByBoardTypeWithAuthor(boardType);
        return posts.stream()
                .map(PostListResponse::from)
                .collect(Collectors.toList());
    }

    /**
     * 게시글 상세 조회 (조회수 증가)
     */
    @Transactional
    public PostResponse getPost(Long postId) {
        Post post = postRepository.findByIdWithAuthor(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        // 조회수 증가
        post.increaseViewCount();

        return PostResponse.from(post);
    }

    /**
     * 게시글 상세 조회 (조회수 증가 X - 수정 시)
     */
    public PostResponse getPostForEdit(Long postId, Long userId) {
        Post post = postRepository.findByIdWithAuthor(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        // 작성자 확인
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        if (!post.isAuthor(user)) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다");
        }

        return PostResponse.from(post);
    }

    /**
     * 게시글 수정
     */
    @Transactional
    public PostResponse updatePost(Long postId, Long userId, PostUpdateRequest request) {
        log.info("게시글 수정 시도: ID {}, 사용자 ID {}", postId, userId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        // 작성자 확인
        if (!post.isAuthor(user)) {
            throw new IllegalArgumentException("게시글 수정 권한이 없습니다");
        }

        String content = htmlSanitizer.sanitize(request.getContent());

        // ReviewPost인 경우
        if (post instanceof ReviewPost && request.getRating() != null) {
            ((ReviewPost) post).updateWithRating(
                    request.getTitle(),
                    content,
                    request.getRating()
            );
            ((ReviewPost) post).validateRating();
        } else {
            post.update(request.getTitle(), content);
        }

        log.info("게시글 수정 완료: ID {}", postId);
        return PostResponse.from(post);
    }

    /**
     * 게시글 삭제
     */
    @Transactional
    public void deletePost(Long postId, Long userId) {
        log.info("게시글 삭제 시도: ID {}, 사용자 ID {}", postId, userId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다"));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다"));

        // 작성자 확인
        if (!post.isAuthor(user)) {
            throw new IllegalArgumentException("게시글 삭제 권한이 없습니다");
        }

        postRepository.delete(post);
        log.info("게시글 삭제 완료: ID {}", postId);
    }

    /**
     * 게시글 검색
     */
    public Page<PostListResponse> searchPosts(BoardType boardType, String keyword, Pageable pageable) {
        Page<Post> posts = postRepository.searchByKeyword(boardType, keyword, pageable);
        return posts.map(PostListResponse::from);
    }

    /**
     * 게시판별 게시글 개수
     */
    public long getPostCount(BoardType boardType) {
        return postRepository.countByBoardType(boardType);
    }
}
