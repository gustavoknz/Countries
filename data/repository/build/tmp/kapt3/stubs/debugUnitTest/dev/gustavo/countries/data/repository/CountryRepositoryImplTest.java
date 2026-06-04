package dev.gustavo.countries.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000<\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\b\b\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\f\u0010\u000f\u001a\u00060\u0010j\u0002`\u0011H\u0007J\f\u0010\u0012\u001a\u00060\u0010j\u0002`\u0011H\u0007J\f\u0010\u0013\u001a\u00060\u0010j\u0002`\u0011H\u0007J\f\u0010\u0014\u001a\u00060\u0010j\u0002`\u0011H\u0007J\f\u0010\u0015\u001a\u00060\u0010j\u0002`\u0011H\u0007J\f\u0010\u0016\u001a\u00060\u0010j\u0002`\u0011H\u0007J\b\u0010\u0017\u001a\u00020\u0010H\u0007J\b\u0010\u0018\u001a\u00020\u0010H\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u000eX\u0082.\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0019"}, d2 = {"Ldev/gustavo/countries/data/repository/CountryRepositoryImplTest;", "", "()V", "api", "Ldev/gustavo/countries/data/remote/api/CountryApiService;", "countryDao", "Ldev/gustavo/countries/data/local/dao/CountryDao;", "countryDetailDao", "Ldev/gustavo/countries/data/local/dao/CountryDetailDao;", "dispatcher", "Lkotlinx/coroutines/test/TestDispatcher;", "fakeDispatcherProvider", "Ldev/gustavo/countries/core/common/DispatcherProvider;", "repository", "Ldev/gustavo/countries/data/repository/CountryRepositoryImpl;", "given api failure when getCountries then returns failure", "", "Lkotlinx/coroutines/test/TestResult;", "given cached detail when getCountryDetail then returns cached data without api call", "given country not in api response when getCountryDetail then returns failure", "given empty cache when getCountries then fetches from api and caches", "given no cached detail when getCountryDetail then fetches from api", "given populated cache when getCountries then returns cached data without api call", "setUp", "tearDown", "repository_debugUnitTest"})
public final class CountryRepositoryImplTest {
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.data.remote.api.CountryApiService api = null;
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.data.local.dao.CountryDao countryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.data.local.dao.CountryDetailDao countryDetailDao = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.test.TestDispatcher dispatcher = null;
    private dev.gustavo.countries.data.repository.CountryRepositoryImpl repository;
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.core.common.DispatcherProvider fakeDispatcherProvider = null;
    
    public CountryRepositoryImplTest() {
        super();
    }
    
    @org.junit.jupiter.api.BeforeEach()
    public final void setUp() {
    }
    
    @org.junit.jupiter.api.AfterEach()
    public final void tearDown() {
    }
}