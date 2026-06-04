package dev.gustavo.countries.data.local.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import dev.gustavo.countries.data.local.dao.CountryDao;
import dev.gustavo.countries.data.local.database.CountriesDatabase;
import javax.annotation.processing.Generated;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation",
    "nullness:initialization.field.uninitialized"
})
public final class LocalModule_ProvideCountryDaoFactory implements Factory<CountryDao> {
  private final Provider<CountriesDatabase> dbProvider;

  private LocalModule_ProvideCountryDaoFactory(Provider<CountriesDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CountryDao get() {
    return provideCountryDao(dbProvider.get());
  }

  public static LocalModule_ProvideCountryDaoFactory create(
      Provider<CountriesDatabase> dbProvider) {
    return new LocalModule_ProvideCountryDaoFactory(dbProvider);
  }

  public static CountryDao provideCountryDao(CountriesDatabase db) {
    return Preconditions.checkNotNullFromProvides(LocalModule.INSTANCE.provideCountryDao(db));
  }
}
