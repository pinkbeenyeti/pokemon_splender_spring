package pokemon.splender.domain.member.service;

import org.springframework.stereotype.Service;
import pokemon.splender.domain.member.repository.MemberRepository;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

}
