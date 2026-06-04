package dev.gustavo.countries.data.local.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import dev.gustavo.countries.data.local.dao.CountryDetailDao;
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
public final class LocalModule_ProvideCountryDetailDaoFactory implements Factory<CountryDetailDao> {
  private final Provider<CountriesDatabase> dbProvider;

  private LocalModule_ProvideCountryDetailDaoFactory(Provider<CountriesDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public CountryDetailDao get() {
    return provideCountryDetailDao(dbProvider.get());
  }

  public static LocalModule_ProvideCountryDetailDaoFactory create(
      Provider<CountriesDatabase> dbProvider) {
    return new LocalModule_ProvideCountryDetailDaoFactory(dbProvider);
  }

  public static CountryDetailDao provideCountryDetailDao(CountriesDatabase db) {
    return Preconditions.checkNotNullFromProvides(LocalModule.INSTANCE.provideCountryDetailDao(db));
  }
}
