package kr.ac.knu.cse.config;

import com.querydsl.core.annotations.Config;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Config
@EnableJpaAuditing
public class JpaAuditingConfig {
}
