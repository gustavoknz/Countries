package dev.gustavo.countries.data.local.di;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import dev.gustavo.countries.data.local.database.CountriesDatabase;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class LocalModule_ProvideDatabaseFactory implements Factory<CountriesDatabase> {
  private final Provider<Context> contextProvider;

  private LocalModule_ProvideDatabaseFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public CountriesDatabase get() {
    return provideDatabase(contextProvider.get());
  }

  public static LocalModule_ProvideDatabaseFactory create(Provider<Context> contextProvider) {
    return new LocalModule_ProvideDatabaseFactory(contextProvider);
  }

  public static CountriesDatabase provideDatabase(Context context) {
    return Preconditions.checkNotNullFromProvides(LocalModule.INSTANCE.provideDatabase(context));
  }
}
