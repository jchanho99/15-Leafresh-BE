package ktb.leafresh.backend.global.security;

import ktb.leafresh.backend.domain.member.infrastructure.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityUtil {

    private final MemberRepository memberRepository;

    // SecurityContext 에 유저 정보가 저장되는 시점
    // Request 가 들어올 때 JwtFilter 의 doFilter 에서 저장
    public Long getCurrentMemberId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || authentication.getName() == null
                || "anonymousUser".equals(authentication.getName())) {
            throw new AuthenticationCredentialsNotFoundException("인증이 필요합니다.");  // 401 Unauthorized 처리
        }

        try {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserDetails userDetails) {

                Long memberId = Long.valueOf(userDetails.getUsername());

                return memberRepository.findById(memberId)
                        .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("존재하지 않는 사용자입니다."))
                        .getId();
            } else {
                throw new AuthenticationCredentialsNotFoundException("잘못된 인증 정보입니다.");
            }
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("인증 정보 처리 중 오류가 발생했습니다.");
        }
    }

    public Long getCurrentMemberIdIfPresent() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()
                || "anonymousUser".equals(authentication.getName())) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails userDetails) {
            try {
                return Long.parseLong(userDetails.getUsername());
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }
}
