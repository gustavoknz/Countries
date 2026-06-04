package dev.gustavo.countries.data.local.di;

@dagger.Module()
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\b\u00c7\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0010\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\u0005\u001a\u00020\u0006H\u0007J\u0012\u0010\t\u001a\u00020\u00062\b\b\u0001\u0010\n\u001a\u00020\u000bH\u0007\u00a8\u0006\f"}, d2 = {"Ldev/gustavo/countries/data/local/di/LocalModule;", "", "()V", "provideCountryDao", "Ldev/gustavo/countries/data/local/dao/CountryDao;", "db", "Ldev/gustavo/countries/data/local/database/CountriesDatabase;", "provideCountryDetailDao", "Ldev/gustavo/countries/data/local/dao/CountryDetailDao;", "provideDatabase", "context", "Landroid/content/Context;", "local_debug"})
@dagger.hilt.InstallIn(value = {dagger.hilt.components.SingletonComponent.class})
public final class LocalModule {
    @org.jetbrains.annotations.NotNull()
    public static final dev.gustavo.countries.data.local.di.LocalModule INSTANCE = null;
    
    private LocalModule() {
        super();
    }
    
    @dagger.Provides()
    @javax.inject.Singleton()
    @org.jetbrains.annotations.NotNull()
    public final dev.gustavo.countries.data.local.database.CountriesDatabase provideDatabase(@dagger.hilt.android.qualifiers.ApplicationContext()
    @org.jetbrains.annotations.NotNull()
    android.content.Context context) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final dev.gustavo.countries.data.local.dao.CountryDao provideCountryDao(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.data.local.database.CountriesDatabase db) {
        return null;
    }
    
    @dagger.Provides()
    @org.jetbrains.annotations.NotNull()
    public final dev.gustavo.countries.data.local.dao.CountryDetailDao provideCountryDetailDao(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.data.local.database.CountriesDatabase db) {
        return null;
    }
}