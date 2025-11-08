package com.infogames.domain.board.repository;

import com.infogames.domain.board.entity.BoardType;
import com.infogames.domain.board.entity.Post;
import com.infogames.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    /**
     * 게시판 타입별 게시글 목록 조회 (페이징)
     */
    Page<Post> findByBoardTypeOrderByCreatedAtDesc(BoardType boardType, Pageable pageable);

    /**
     * 게시판 타입별 게시글 목록 조회 (작성자 정보 함께 - N+1 해결)
     */
    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.boardType = :boardType ORDER BY p.createdAt DESC")
    List<Post> findByBoardTypeWithAuthor(@Param("boardType") BoardType boardType);

    /**
     * 특정 사용자의 게시글 목록
     */
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);

    /**
     * 게시글 상세 조회 (작성자 정보 함께)
     */
    @Query("SELECT p FROM Post p JOIN FETCH p.author WHERE p.id = :id")
    Optional<Post> findByIdWithAuthor(@Param("id") Long id);

    /**
     * 제목으로 검색 (페이징)
     */
    Page<Post> findByBoardTypeAndTitleContainingOrderByCreatedAtDesc(
            BoardType boardType, String title, Pageable pageable);

    /**
     * 내용으로 검색 (페이징)
     */
    Page<Post> findByBoardTypeAndContentContainingOrderByCreatedAtDesc(
            BoardType boardType, String content, Pageable pageable);

    /**
     * 제목 + 내용 검색 (페이징)
     */
    @Query("SELECT p FROM Post p WHERE p.boardType = :boardType " +
           "AND (p.title LIKE %:keyword% OR p.content LIKE %:keyword%) " +
           "ORDER BY p.createdAt DESC")
    Page<Post> searchByKeyword(@Param("boardType") BoardType boardType, 
                                @Param("keyword") String keyword, 
                                Pageable pageable);

    /**
     * 조회수 증가
     */
    @Modifying
    @Query("UPDATE Post p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
    void incrementViewCount(@Param("id") Long id);

    /**
     * 게시판별 게시글 개수
     */
    long countByBoardType(BoardType boardType);
}
