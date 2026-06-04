package dev.gustavo.countries.data.repository.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\b\'\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\'J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\tH\'\u00a8\u0006\n"}, d2 = {"Ldev/gustavo/countries/data/repository/di/RepositoryModule;", "", "()V", "bindCountryRepository", "Ldev/gustavo/countries/domain/repository/CountryRepository;", "impl", "Ldev/gustavo/countries/data/repository/CountryRepositoryImpl;", "bindDispatcherProvider", "Ldev/gustavo/countries/core/common/DispatcherProvider;", "Ldev/gustavo/countries/core/common/DefaultDispatcherProvider;", "repository_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public abstract class RepositoryModule {
    
    public RepositoryModule() {
        super();
    }
    
    @dagger.Binds()
    @org.jetbrains.annotations.NotNull()
    public abstract dev.gustavo.countries.domain.repository.CountryRepository bindCountryRepository(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.data.repository.CountryRepositoryImpl impl);
    
    @dagger.Binds()
    @org.jetbrains.annotations.NotNull()
    public abstract dev.gustavo.countries.core.common.DispatcherProvider bindDispatcherProvider(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.core.common.DefaultDispatcherProvider impl);
}