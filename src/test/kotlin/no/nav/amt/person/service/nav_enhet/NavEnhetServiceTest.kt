package no.nav.amt.person.service.nav_enhet

import io.kotest.assertions.assertSoftly
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import no.nav.amt.person.service.clients.norg.NorgClient
import no.nav.amt.person.service.clients.norg.NorgNavEnhet
import no.nav.amt.person.service.clients.veilarbarena.VeilarbarenaClient
import no.nav.amt.person.service.data.TestData
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NavEnhetServiceTest {
	private val norgClient: NorgClient = mockk()
	private val navEnhetRepository: NavEnhetRepository = mockk(relaxUnitFun = true)
	private val veilarbarenaClient: VeilarbarenaClient = mockk()

	private val service = NavEnhetService(
		navEnhetRepository = navEnhetRepository,
		norgClient = norgClient,
		veilarbarenaClient = veilarbarenaClient,
	)

	@BeforeEach
	fun setup() = clearAllMocks()

	@Test
	fun `hentNavEnhetForBruker - enhet finnes ikke - skal opprette enhet`() {
		val enhet = TestData.lagNavEnhet()
		val personident = "FNR"

		every { veilarbarenaClient.hentBrukerOppfolgingsenhetId(personident) } returns enhet.enhetId
		every { navEnhetRepository.get(enhet.enhetId) } returns null
		every { norgClient.hentNavEnhet(enhet.enhetId) } returns NorgNavEnhet(enhet.enhetId, enhet.navn)

		val faktiskEnhet = service.hentNavEnhetForBruker(personident)!!
		assertSoftly(faktiskEnhet.shouldNotBeNull()) {
			enhetId shouldBe enhet.enhetId
			navn shouldBe enhet.navn
		}
	}
}
