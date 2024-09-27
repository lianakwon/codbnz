package we.cod.bnz.account.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
public class QueryDslConfig {

  @PersistenceContext
  private EntityManager em;

  @Bean
  JPAQueryFactory jpaRepository() {
    return new JPAQueryFactory(em);
  }
}
