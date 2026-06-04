package dev.gustavo.countries.data.repository.di;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import dev.gustavo.countries.domain.repository.CountryRepository;
import dev.gustavo.countries.domain.usecase.GetCountryDetailUseCase;
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
public final class UseCaseModule_ProvideGetCountryDetailUseCaseFactory implements Factory<GetCountryDetailUseCase> {
  private final Provider<CountryRepository> repositoryProvider;

  private UseCaseModule_ProvideGetCountryDetailUseCaseFactory(
      Provider<CountryRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetCountryDetailUseCase get() {
    return provideGetCountryDetailUseCase(repositoryProvider.get());
  }

  public static UseCaseModule_ProvideGetCountryDetailUseCaseFactory create(
      Provider<CountryRepository> repositoryProvider) {
    return new UseCaseModule_ProvideGetCountryDetailUseCaseFactory(repositoryProvider);
  }

  public static GetCountryDetailUseCase provideGetCountryDetailUseCase(
      CountryRepository repository) {
    return Preconditions.checkNotNullFromProvides(UseCaseModule.INSTANCE.provideGetCountryDetailUseCase(repository));
  }
}
