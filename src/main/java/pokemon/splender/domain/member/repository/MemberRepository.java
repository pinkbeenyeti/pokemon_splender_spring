package pokemon.splender.domain.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pokemon.splender.domain.member.entity.Member;


public interface MemberRepository extends JpaRepository<Member, Long> {

}
