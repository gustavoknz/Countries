package dev.gustavo.countries.data.repository;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\'\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0007\u0012\u0006\u0010\b\u001a\u00020\t\u00a2\u0006\u0002\u0010\nJ\"\u0010\u000b\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\u000e0\r0\fH\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u000f\u0010\u0010J$\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\u00120\f2\u0006\u0010\u0013\u001a\u00020\u0014H\u0096@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\u0015\u0010\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006\u0017"}, d2 = {"Ldev/gustavo/countries/data/repository/CountryRepositoryImpl;", "Ldev/gustavo/countries/domain/repository/CountryRepository;", "api", "Ldev/gustavo/countries/data/remote/api/CountryApiService;", "countryDao", "Ldev/gustavo/countries/data/local/dao/CountryDao;", "countryDetailDao", "Ldev/gustavo/countries/data/local/dao/CountryDetailDao;", "dispatchers", "Ldev/gustavo/countries/core/common/DispatcherProvider;", "(Ldev/gustavo/countries/data/remote/api/CountryApiService;Ldev/gustavo/countries/data/local/dao/CountryDao;Ldev/gustavo/countries/data/local/dao/CountryDetailDao;Ldev/gustavo/countries/core/common/DispatcherProvider;)V", "getCountries", "Lkotlin/Result;", "", "Ldev/gustavo/countries/domain/model/Country;", "getCountries-IoAF18A", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCountryDetail", "Ldev/gustavo/countries/domain/model/CountryDetail;", "cca3", "", "getCountryDetail-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "repository_debug"})
public final class CountryRepositoryImpl implements dev.gustavo.countries.domain.repository.CountryRepository {
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.data.remote.api.CountryApiService api = null;
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.data.local.dao.CountryDao countryDao = null;
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.data.local.dao.CountryDetailDao countryDetailDao = null;
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.core.common.DispatcherProvider dispatchers = null;
    
    @javax.inject.Inject()
    public CountryRepositoryImpl(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.data.remote.api.CountryApiService api, @org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.data.local.dao.CountryDao countryDao, @org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.data.local.dao.CountryDetailDao countryDetailDao, @org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.core.common.DispatcherProvider dispatchers) {
        super();
    }
}