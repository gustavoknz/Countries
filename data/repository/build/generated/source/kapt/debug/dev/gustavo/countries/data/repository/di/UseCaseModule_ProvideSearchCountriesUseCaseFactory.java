package dev.gustavo.countries.data.repository.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import dev.gustavo.countries.domain.repository.CountryRepository;
import dev.gustavo.countries.domain.usecase.SearchCountriesUseCase;
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
public final class UseCaseModule_ProvideSearchCountriesUseCaseFactory implements Factory<SearchCountriesUseCase> {
  private final Provider<CountryRepository> repositoryProvider;

  private UseCaseModule_ProvideSearchCountriesUseCaseFactory(
      Provider<CountryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SearchCountriesUseCase get() {
    return provideSearchCountriesUseCase(repositoryProvider.get());
  }

  public static UseCaseModule_ProvideSearchCountriesUseCaseFactory create(
      Provider<CountryRepository> repositoryProvider) {
    return new UseCaseModule_ProvideSearchCountriesUseCaseFactory(repositoryProvider);
  }

  public static SearchCountriesUseCase provideSearchCountriesUseCase(CountryRepository repository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideSearchCountriesUseCase(repository));
  }
}
