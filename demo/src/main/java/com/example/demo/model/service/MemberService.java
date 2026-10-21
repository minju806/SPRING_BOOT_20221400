package com.example.demo.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.*; // User, UserDetails 등
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import com.example.demo.model.domain.Member;
import com.example.demo.model.dto.MemberForm;
import com.example.demo.model.repository.MemberRepository;

@Service
public class MemberService implements UserDetailsService { // 로그인을 위한 인터페이스 제공

    @Autowired
    private MemberRepository memberRepository;

    @Autowired // SecurityConfig 에 등록한 BCryptPasswordEncoder 주입
    private PasswordEncoder passwordEncoder;

    public Member signup(MemberForm form) { // 회원가입
        // [7주차] JSON 요청에는 HTML required 검사가 없으므로 빈 값 직접 검사
        if (isBlank(form.getUsername()) || isBlank(form.getPassword()) || isBlank(form.getName())) {
            throw new IllegalArgumentException("아이디, 비밀번호, 이름은 필수입니다.");
        }

        if (!form.getPassword().equals(form.getPasswordConfirm())) { // 비밀번호 확인
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (memberRepository.existsByUsername(form.getUsername())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        Member member = new Member();
        member.setUsername(form.getUsername());
        member.setPassword(passwordEncoder.encode(form.getPassword())); // ★ 암호화
        member.setName(form.getName());
        member.setRole("USER"); // 기본 권한

        return memberRepository.save(member); // INSERT
    }

    // 로그인한 아이디로 회원 조회
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException(
                        "회원이 존재하지 않습니다 : " + username));
    }

    @Override // 로그인 시 시큐리티가 자동 호출
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("회원 없음 : " + username));

        return User.builder() // 시큐리티용 사용자 객체로 변환
                .username(member.getUsername())
                .password(member.getPassword()) // 암호화 값 → matches()로 비교
                .roles(member.getRole()) // USER → ROLE_USER
                .build();
    }

    public List<Member> findAll() { // 전체 회원 (id 순서)
        return memberRepository.findAll(Sort.by("id"));
    }

    public List<Member> search(String keyword) { // [7주차 연습①] 회원 검색
        return memberRepository.findByUsernameContainingOrNameContaining(keyword, keyword);
    }

    public Optional<Member> findById(Long id) { // 회원 1명 (없을 수도 있음)
        return memberRepository.findById(id);
    }

    // 관리자가 부여할 수 있는 권한 (화면에서 넘어온 값을 그대로 믿지 않는다)
    private static final List<String> ROLES = List.of("USER", "MANAGER", "ADMIN");

    @PreAuthorize("hasRole('ADMIN')") // 관리자만 실행 가능 (2차 잠금)
    public Member changeRole(Long id, String role) { // 권한 변경 ([7주차] void → Member)
        if (!ROLES.contains(role)) { // 허용된 권한만
            throw new IllegalArgumentException("허용되지 않는 권한입니다 : " + role);
        }
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        checkNotSelf(member); // [연습①] 본인 보호
        member.setRole(role);
        return memberRepository.save(member); // UPDATE + [7주차] 바뀐 회원을 돌려준다
    }

    @PreAuthorize("hasRole('ADMIN')") // 관리자만 실행 가능 (2차 잠금)
    public void deleteMember(Long id) { // 회원 삭제
        Member member = memberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));
        checkNotSelf(member); // [연습①] 본인 보호
        memberRepository.delete(member); // DELETE
    }

    // [연습①] 로그인한 본인이면 거부 (관리자 0명 방지)
    private void checkNotSelf(Member member) {
        String me = SecurityContextHolder.getContext().getAuthentication().getName();
        if (member.getUsername().equals(me)) {
            throw new IllegalArgumentException("자기 자신의 권한 변경·삭제는 할 수 없습니다.");
        }
    }

    // [7주차] 빈 값 검사 (null 또는 공백)
    private boolean isBlank(String value) { return value == null || value.isBlank(); }
}