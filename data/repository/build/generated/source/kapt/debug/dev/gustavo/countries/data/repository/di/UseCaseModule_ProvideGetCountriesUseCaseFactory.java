package dev.gustavo.countries.data.repository.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import dev.gustavo.countries.domain.repository.CountryRepository;
import dev.gustavo.countries.domain.usecase.GetCountriesUseCase;
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
public final class UseCaseModule_ProvideGetCountriesUseCaseFactory implements Factory<GetCountriesUseCase> {
  private final Provider<CountryRepository> repositoryProvider;

  private UseCaseModule_ProvideGetCountriesUseCaseFactory(
      Provider<CountryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetCountriesUseCase get() {
    return provideGetCountriesUseCase(repositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetCountriesUseCaseFactory create(
      Provider<CountryRepository> repositoryProvider) {
    return new UseCaseModule_ProvideGetCountriesUseCaseFactory(repositoryProvider);
  }

  public static GetCountriesUseCase provideGetCountriesUseCase(CountryRepository repository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetCountriesUseCase(repository));
  }
}
