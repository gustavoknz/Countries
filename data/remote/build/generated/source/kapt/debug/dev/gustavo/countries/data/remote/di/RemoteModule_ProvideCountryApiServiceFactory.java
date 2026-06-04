package dev.gustavo.countries.data.remote.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import dev.gustavo.countries.data.remote.api.CountryApiService;
import javax.annotation.processing.Generated;
import retrofit2.Retrofit;

@ScopeMetadata("javax.inject.Singleton")
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
public final class RemoteModule_ProvideCountryApiServiceFactory implements Factory<CountryApiService> {
  private final Provider<Retrofit> retrofitProvider;

  private RemoteModule_ProvideCountryApiServiceFactory(Provider<Retrofit> retrofitProvider) {
    this.retrofitProvider = retrofitProvider;
  }

  @Override
  public CountryApiService get() {
    return provideCountryApiService(retrofitProvider.get());
  }

  public static RemoteModule_ProvideCountryApiServiceFactory create(
      Provider<Retrofit> retrofitProvider) {
    return new RemoteModule_ProvideCountryApiServiceFactory(retrofitProvider);
  }

  public static CountryApiService provideCountryApiService(Retrofit retrofit) {
    return Preconditions.checkNotNullFromProvides(RemoteModule.INSTANCE.provideCountryApiService(retrofit));
  }
}
