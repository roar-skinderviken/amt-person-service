package no.nav.amt.person.service.nav_enhet

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import no.nav.amt.person.service.data.RepositoryTestBase
import no.nav.amt.person.service.data.TestData
import no.nav.amt.person.service.utils.shouldBeEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.boot.test.context.SpringBootTest
import java.util.UUID

@SpringBootTest(classes = [NavEnhetRepository::class])
class NavEnhetRepositoryTest(
	private val enhetRepository: NavEnhetRepository,
) : RepositoryTestBase() {

	@Test
	fun `get(uuid) - enhet finnes - returnerer enhet`() {
		val enhet = TestData.lagNavEnhet()
		testDataRepository.insertNavEnhet(enhet)

		val faktiskEnhet = enhetRepository.get(enhet.id)

		faktiskEnhet.id shouldBe enhet.id
		faktiskEnhet.enhetId shouldBe enhet.enhetId
		faktiskEnhet.navn shouldBe enhet.navn
		faktiskEnhet.createdAt shouldBeEqualTo enhet.createdAt
		faktiskEnhet.modifiedAt shouldBeEqualTo enhet.modifiedAt
	}

	@Test
	fun `get(uuid) - enhet finnes ikke - kaster NoSuchElementException`() {
		assertThrows<NoSuchElementException> {
			enhetRepository.get(UUID.randomUUID())
		}
	}

	@Test
	fun `get(enhetId) - enhet finnes - returnerer enhet`() {
		val enhet = TestData.lagNavEnhet()
		testDataRepository.insertNavEnhet(enhet)

		val faktiskEnhet = enhetRepository.get(enhet.enhetId)
		assertSoftly(faktiskEnhet.shouldNotBeNull()) {
			id shouldBe enhet.id
			enhetId shouldBe enhet.enhetId
			navn shouldBe enhet.navn
			createdAt shouldBeEqualTo enhet.createdAt
			modifiedAt shouldBeEqualTo enhet.modifiedAt
		}
	}

	@Test
	fun `get(enhetId) - enhet finnes ikke - returnerer null`() {
		enhetRepository.get("Ikke Eksisternede Enhet") shouldBe null
	}

	@Test
	fun `insert - ny enhet - inserter ny enhet`() {
		val enhet = NavEnhet(
			id = UUID.randomUUID(),
			enhetId = "0001",
			navn = "Ny Nav Enhet",
		)

		enhetRepository.insert(enhet)

		val faktiskEnhet = enhetRepository.get(enhet.id)

		faktiskEnhet.id shouldBe enhet.id
		faktiskEnhet.enhetId shouldBe enhet.enhetId
		faktiskEnhet.navn shouldBe enhet.navn
	}
}
