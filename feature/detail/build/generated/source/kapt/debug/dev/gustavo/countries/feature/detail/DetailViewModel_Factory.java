package dev.gustavo.countries.feature.detail;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class DetailViewModel_Factory implements Factory<DetailViewModel> {
  private final Provider<GetCountryDetailUseCase> getCountryDetailUseCaseProvider;

  private DetailViewModel_Factory(
      Provider<GetCountryDetailUseCase> getCountryDetailUseCaseProvider) {
    this.getCountryDetailUseCaseProvider = getCountryDetailUseCaseProvider;
  }

  @Override
  public DetailViewModel get() {
    return newInstance(getCountryDetailUseCaseProvider.get());
  }

  public static DetailViewModel_Factory create(
      Provider<GetCountryDetailUseCase> getCountryDetailUseCaseProvider) {
    return new DetailViewModel_Factory(getCountryDetailUseCaseProvider);
  }

  public static DetailViewModel newInstance(GetCountryDetailUseCase getCountryDetailUseCase) {
    return new DetailViewModel(getCountryDetailUseCase);
  }
}
