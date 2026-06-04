package dev.gustavo.countries.data.repository.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\t\u001a\u00020\n2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007\u00a8\u0006\u000b"}, d2 = {"Ldev/gustavo/countries/data/repository/di/UseCaseModule;", "", "()V", "provideGetCountriesUseCase", "Ldev/gustavo/countries/domain/usecase/GetCountriesUseCase;", "repository", "Ldev/gustavo/countries/domain/repository/CountryRepository;", "provideGetCountryDetailUseCase", "Ldev/gustavo/countries/domain/usecase/GetCountryDetailUseCase;", "provideSearchCountriesUseCase", "Ldev/gustavo/countries/domain/usecase/SearchCountriesUseCase;", "repository_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class UseCaseModule {
    @org.jetbrains.annotations.NotNull()
    public static final dev.gustavo.countries.data.repository.di.UseCaseModule INSTANCE = null;
    
    private UseCaseModule() {
        super();
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final dev.gustavo.countries.domain.usecase.GetCountriesUseCase provideGetCountriesUseCase(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.domain.repository.CountryRepository repository) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final dev.gustavo.countries.domain.usecase.SearchCountriesUseCase provideSearchCountriesUseCase(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.domain.repository.CountryRepository repository) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final dev.gustavo.countries.domain.usecase.GetCountryDetailUseCase provideGetCountryDetailUseCase(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.domain.repository.CountryRepository repository) {
        return null;
    }
}