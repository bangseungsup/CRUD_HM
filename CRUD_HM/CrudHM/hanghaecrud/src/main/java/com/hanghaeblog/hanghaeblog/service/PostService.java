package com.hanghaeblog.hanghaeblog.service;


import com.hanghaeblog.hanghaeblog.dto.PostRequestDto;
import com.hanghaeblog.hanghaeblog.dto.PostResponseDto;
import com.hanghaeblog.hanghaeblog.entity.Post;
import com.hanghaeblog.hanghaeblog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional // 데이터들을 넣어주려면 비워내야하기 때문에 new 사용
    public Post createPost(PostRequestDto requestDto) {
        Post post = new Post(requestDto);
        postRepository.save(post);
        return post;
    }

    @Transactional // 리스트 형태로 받아와야하기 때문에 List<Post>
    public List<Post> getPosts() {
        return postRepository.findAllByOrderByModifiedAtDesc();
    }

    @Transactional // 수정 및 삭제 findById(id).orElseThrow
    public PostResponseDto update(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        boolean result = requestDto.getPassword().equals(post.getPassword()); // equals 사용, password가 맞다면 업데이트 허용
        if (result) {
            post.update(requestDto);
        }
        return new PostResponseDto(post);

    }


    @Transactional
    public String deletePost(Long id, PostRequestDto requestDto) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 없습니다.")
        );
        boolean result = requestDto.getPassword().equals(post.getPassword()); // 업데이트와 동일, 조건에 따라 다른 답변을 줘야 해기 때문에 ""
        String reply = "";
        if (result) {
            postRepository.deleteById(id);
            reply = "삭제완료";
        } else {
            System.out.println("비밀번호가 일치하지않습니다.");
            reply = "비밀번호가 일치하지않습니다.";
        }
        return reply;
    }


    @Transactional
    public PostResponseDto getPost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시글이 존재하지 않습니다.")
        );
        return new PostResponseDto(post);
    }
}
