package tech.jaya.ridely

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ActiveProfiles
import tech.jaya.ridely.domain.repository.DriverRepo
import tech.jaya.ridely.domain.repository.RideRepo

@ActiveProfiles("test")
@SpringBootTest(
	classes = [RidelyApplication::class],
	properties = [
		"spring.autoconfigure.exclude=" +
				"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration," +
				"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration," +
				"org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration"
	]
)
@MockBean(DriverRepo::class)
@MockBean(RideRepo::class)
class RidelyApplicationTests {

	@Test
	fun contextLoads() {
		// apenas sobe o contexto
	}
}
