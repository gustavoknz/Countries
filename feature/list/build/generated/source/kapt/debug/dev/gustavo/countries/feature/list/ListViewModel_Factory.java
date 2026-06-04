package dev.gustavo.countries.feature.list;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import dev.gustavo.countries.domain.usecase.GetCountriesUseCase;
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
public final class ListViewModel_Factory implements Factory<ListViewModel> {
  private final Provider<GetCountriesUseCase> getCountriesUseCaseProvider;

  private final Provider<SearchCountriesUseCase> searchCountriesUseCaseProvider;

  private ListViewModel_Factory(Provider<GetCountriesUseCase> getCountriesUseCaseProvider,
      Provider<SearchCountriesUseCase> searchCountriesUseCaseProvider) {
    this.getCountriesUseCaseProvider = getCountriesUseCaseProvider;
    this.searchCountriesUseCaseProvider = searchCountriesUseCaseProvider;
  }

  @Override
  public ListViewModel get() {
    return newInstance(getCountriesUseCaseProvider.get(), searchCountriesUseCaseProvider.get());
  }

  public static ListViewModel_Factory create(
      Provider<GetCountriesUseCase> getCountriesUseCaseProvider,
      Provider<SearchCountriesUseCase> searchCountriesUseCaseProvider) {
    return new ListViewModel_Factory(getCountriesUseCaseProvider, searchCountriesUseCaseProvider);
  }

  public static ListViewModel newInstance(GetCountriesUseCase getCountriesUseCase,
      SearchCountriesUseCase searchCountriesUseCase) {
    return new ListViewModel(getCountriesUseCase, searchCountriesUseCase);
  }
}
