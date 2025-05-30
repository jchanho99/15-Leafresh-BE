package ktb.leafresh.backend.domain.challenge.group.infrastructure.dto.request;

import java.util.List;

public record AiChallengeValidationRequestDto(
        Long memberId,
        String challengeName,
        String startDate,
        String endDate,
        List<ChallengeSummary> challenge
) {
    public record ChallengeSummary(
            Long id,
            String name,
            String startDate,
            String endDate
    ) {
        @Override
        public String toString() {
            return String.format("{id=%d, name='%s', startDate=%s, endDate=%s}", id, name, startDate, endDate);
        }
    }
}
