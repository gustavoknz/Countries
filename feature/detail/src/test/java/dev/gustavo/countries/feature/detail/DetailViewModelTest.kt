package dev.gustavo.countries.feature.detail

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dev.gustavo.countries.domain.model.CountryDetail
import dev.gustavo.countries.domain.usecase.GetCountryDetailUseCase
import dev.gustavo.countries.feature.detail.model.toUiModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

class DetailViewModelTest {

    private val getCountryDetailUseCase: GetCountryDetailUseCase = mockk()
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
        currencies = listOf("Brazilian real")
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(UnconfinedTestDispatcher())
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
            assertThat(awaitItem()).isInstanceOf(DetailViewState.Loading::class.java)
            viewModel.onAction(DetailAction.LoadDetail("BRA"))
            val loaded = awaitItem() as DetailViewState.Loaded
            assertThat(loaded.country).isEqualTo(countryDetail.toUiModel())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given failure when LoadDetail then viewState is Error`() = runTest {
        coEvery { getCountryDetailUseCase("XYZ") } returns Result.failure(RuntimeException("Not found"))

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(DetailViewState.Loading::class.java)
            viewModel.onAction(DetailAction.LoadDetail("XYZ"))
            val error = awaitItem() as DetailViewState.Error
            assertThat(error.message).isEqualTo("Not found")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given success when LoadDetail then use case is called with correct cca3`() = runTest {
        coEvery { getCountryDetailUseCase("BRA") } returns Result.success(countryDetail)

        viewModel.onAction(DetailAction.LoadDetail("BRA"))

        coVerify(exactly = 1) { getCountryDetailUseCase("BRA") }
    }

    @Test
    fun `given error state when LoadDetail retried then transitions to Loading then Loaded`() = runTest {
        coEvery { getCountryDetailUseCase("BRA") } returnsMany listOf(
            Result.failure(RuntimeException("Timeout")),
            Result.success(countryDetail),
        )

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(DetailViewState.Loading::class.java)

            viewModel.onAction(DetailAction.LoadDetail("BRA"))
            assertThat(awaitItem()).isInstanceOf(DetailViewState.Error::class.java)

            viewModel.onAction(DetailAction.LoadDetail("BRA"))
            val loaded = awaitItem() as DetailViewState.Loaded
            assertThat(loaded.country.cca3).isEqualTo("BRA")
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `given null error message when LoadDetail fails then Error has fallback message`() = runTest {
        coEvery { getCountryDetailUseCase("BRA") } returns Result.failure(RuntimeException())

        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(DetailViewState.Loading::class.java)
            viewModel.onAction(DetailAction.LoadDetail("BRA"))
            val error = awaitItem() as DetailViewState.Error
            assertThat(error.message).isEqualTo("Unknown error")
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

    @Test
    fun `given back action called twice when BackClicked then emits two NavigateBack events`() = runTest {
        viewModel.events.test {
            viewModel.onAction(DetailAction.BackClicked)
            viewModel.onAction(DetailAction.BackClicked)
            assertThat(awaitItem()).isEqualTo(DetailEvent.NavigateBack)
            assertThat(awaitItem()).isEqualTo(DetailEvent.NavigateBack)
        }
    }

    // ── Initial state ─────────────────────────────────────────────────────────

    @Test
    fun `given fresh viewModel then initial state is Loading`() = runTest {
        viewModel.viewState.test {
            assertThat(awaitItem()).isInstanceOf(DetailViewState.Loading::class.java)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
