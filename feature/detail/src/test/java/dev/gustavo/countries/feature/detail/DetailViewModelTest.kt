package dev.gustavo.countries.feature.detail

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.core.ui.util.UiText
import dev.gustavo.countries.domain.model.CountryDetail
import dev.gustavo.countries.domain.usecase.GetCountryDetailUseCase
import dev.gustavo.countries.feature.detail.model.toUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds
import dev.gustavo.countries.core.ui.R as UiR

class DetailViewModelTest {

    private val getCountryDetailUseCase: GetCountryDetailUseCase = mockk()
    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: DetailViewModel

    private val countryDetail = CountryDetail(
        cca3 = "BRA",
        commonName = "Brazil",
        officialName = "Federative Republic of Brazil",
        capital = "Brasília",
        flagUrl = "https://flagcdn.com/br.png",
        region = "Americas",
        subregion = "South America",
        languages = listOf("Portuguese"),
        population = 215_000_000L,
        borders = listOf("ARG", "BOL", "COL", "GUF", "GUY", "PRY", "PER", "SUR", "URY", "VEN"),
        currencies = listOf("Brazilian real"),
        independent = true
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = DetailViewModel(getCountryDetailUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // ── LoadDetail ────────────────────────────────────────────────────────────

    @Test
    fun `given success when LoadDetail then viewState is Loaded with country`() = runTest {
        coEvery { getCountryDetailUseCase("BRA") } returns Result.success(countryDetail)

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading())
            viewModel.onAction(DetailAction.LoadDetail("BRA", "url"))
            
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading("BRA", "url"))
            
            runCurrent()
            val loaded = awaitItem() as DetailViewState.Loaded
            assertThat(loaded.country).isEqualTo(countryDetail.toUiModel())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given failure when LoadDetail then viewState is Error`() = runTest {
        coEvery { getCountryDetailUseCase("XYZ") } returns Result.failure(RuntimeException("Not found"))

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading())
            viewModel.onAction(DetailAction.LoadDetail("XYZ"))
            
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading("XYZ", null))
            
            runCurrent()
            val error = awaitItem() as DetailViewState.Error
            assertThat(error.message).isInstanceOf(UiText.StringResource::class.java)
            assertThat((error.message as UiText.StringResource).resId).isEqualTo(UiR.string.error_unknown)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given success when LoadDetail then use case is called with correct cca3`() = runTest {
        coEvery { getCountryDetailUseCase("BRA") } returns Result.success(countryDetail)

        viewModel.onAction(DetailAction.LoadDetail("BRA"))
        runCurrent()

        coVerify(exactly = 1) { getCountryDetailUseCase("BRA") }
    }

    @Test
    fun `given multiple calls when LoadDetail then previous jobs are cancelled`() = runTest {
        coEvery { getCountryDetailUseCase(any()) } coAnswers {
            val cca3 = it.invocation.args[0] as String
            delay(1.seconds)
            Result.success(countryDetail.copy(cca3 = cca3))
        }

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading())

            viewModel.onAction(DetailAction.LoadDetail("BRA"))
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading("BRA", null))
            
            runCurrent()
            advanceTimeBy(500.milliseconds)
            runCurrent()

            // Trigger second call while first is still pending
            viewModel.onAction(DetailAction.LoadDetail("PRT"))
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading("PRT", null))
            
            runCurrent()
            advanceUntilIdle()

            // Only one Loaded state should be emitted (from the second call "PRT")
            val loaded = awaitItem() as DetailViewState.Loaded
            assertThat(loaded.country.cca3).isEqualTo("PRT")
            expectNoEvents()
        }
    }

    @Test
    fun `given error state when LoadDetail retried then transitions to Loading then Loaded`() = runTest {
        coEvery { getCountryDetailUseCase("BRA") } returnsMany listOf(
            Result.failure(RuntimeException("Timeout")),
            Result.success(countryDetail)
        )

        viewModel.viewState.test {
            // Initial state
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading())

            // First attempt: Error
            viewModel.onAction(DetailAction.LoadDetail("BRA"))
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading("BRA", null))
            runCurrent()
            assertThat(awaitItem()).isInstanceOf(DetailViewState.Error::class.java)

            // Second attempt: Loading -> Loaded
            viewModel.onAction(DetailAction.LoadDetail("BRA"))
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading("BRA", null))
            
            runCurrent()
            val loaded = awaitItem() as DetailViewState.Loaded
            assertThat(loaded.country.cca3).isEqualTo("BRA")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given null error message when LoadDetail fails then Error has fallback message`() = runTest {
        coEvery { getCountryDetailUseCase("BRA") } returns Result.failure(RuntimeException())

        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading())
            viewModel.onAction(DetailAction.LoadDetail("BRA"))
            
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading("BRA", null))
            
            runCurrent()
            val error = awaitItem() as DetailViewState.Error
            assertThat(error.message).isInstanceOf(UiText.StringResource::class.java)
            assertThat((error.message as UiText.StringResource).resId).isEqualTo(UiR.string.error_unknown)
            cancelAndIgnoreRemainingEvents()
        }
    }

    // ── BackClicked ───────────────────────────────────────────────────────────

    @Test
    fun `given back action when BackClicked then emits NavigateBack event`() = runTest {
        viewModel.events.test {
            viewModel.onAction(DetailAction.BackClicked)
            assertThat(awaitItem()).isEqualTo(DetailEvent.NavigateBack)
        }
    }

    // ── Initial state ─────────────────────────────────────────────────────────

    @Test
    fun `given fresh viewModel then initial state is Loading`() = runTest {
        viewModel.viewState.test {
            assertThat(awaitItem()).isEqualTo(DetailViewState.Loading())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
