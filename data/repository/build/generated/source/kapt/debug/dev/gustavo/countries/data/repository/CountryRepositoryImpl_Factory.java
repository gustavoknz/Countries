package dev.gustavo.countries.data.repository;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import dev.gustavo.countries.core.common.DispatcherProvider;
import dev.gustavo.countries.data.local.dao.CountryDao;
import dev.gustavo.countries.data.local.dao.CountryDetailDao;
import dev.gustavo.countries.data.remote.api.CountryApiService;
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
public final class CountryRepositoryImpl_Factory implements Factory<CountryRepositoryImpl> {
  private final Provider<CountryApiService> apiProvider;

  private final Provider<CountryDao> countryDaoProvider;

  private final Provider<CountryDetailDao> countryDetailDaoProvider;

  private final Provider<DispatcherProvider> dispatchersProvider;

  private CountryRepositoryImpl_Factory(Provider<CountryApiService> apiProvider,
      Provider<CountryDao> countryDaoProvider, Provider<CountryDetailDao> countryDetailDaoProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    this.apiProvider = apiProvider;
    this.countryDaoProvider = countryDaoProvider;
    this.countryDetailDaoProvider = countryDetailDaoProvider;
    this.dispatchersProvider = dispatchersProvider;
  }

  @Override
  public CountryRepositoryImpl get() {
    return newInstance(apiProvider.get(), countryDaoProvider.get(), countryDetailDaoProvider.get(), dispatchersProvider.get());
  }

  public static CountryRepositoryImpl_Factory create(Provider<CountryApiService> apiProvider,
      Provider<CountryDao> countryDaoProvider, Provider<CountryDetailDao> countryDetailDaoProvider,
      Provider<DispatcherProvider> dispatchersProvider) {
    return new CountryRepositoryImpl_Factory(apiProvider, countryDaoProvider, countryDetailDaoProvider, dispatchersProvider);
  }

  public static CountryRepositoryImpl newInstance(CountryApiService api, CountryDao countryDao,
      CountryDetailDao countryDetailDao, DispatcherProvider dispatchers) {
    return new CountryRepositoryImpl(api, countryDao, countryDetailDao, dispatchers);
  }
}
