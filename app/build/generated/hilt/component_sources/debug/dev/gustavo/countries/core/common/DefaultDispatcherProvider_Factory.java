package dev.gustavo.countries.core.common;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
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
public final class DefaultDispatcherProvider_Factory implements Factory<DefaultDispatcherProvider> {
  @Override
  public DefaultDispatcherProvider get() {
    return newInstance();
  }

  public static DefaultDispatcherProvider_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static DefaultDispatcherProvider newInstance() {
    return new DefaultDispatcherProvider();
  }

  private static final class InstanceHolder {
    static final DefaultDispatcherProvider_Factory INSTANCE = new DefaultDispatcherProvider_Factory();
  }
}
