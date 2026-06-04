package dev.gustavo.countries.data.remote.api;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001c\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\bf\u0018\u00002\u00020\u0001J\u001e\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0003\u0010\u0005\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007J\u001e\u0010\b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\b\b\u0001\u0010\t\u001a\u00020\u0006H\u00a7@\u00a2\u0006\u0002\u0010\u0007\u00a8\u0006\n"}, d2 = {"Ldev/gustavo/countries/data/remote/api/CountryApiService;", "", "getAllCountries", "", "Ldev/gustavo/countries/data/remote/model/CountryRemote;", "fields", "", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getCountryDetail", "cca3", "remote_debug"})
public abstract interface CountryApiService {
    
    @retrofit2.http.GET(value = "all")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getAllCountries(@retrofit2.http.Query(value = "fields")
    @org.jetbrains.annotations.NotNull()
    java.lang.String fields, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<dev.gustavo.countries.data.remote.model.CountryRemote>> $completion);
    
    @retrofit2.http.GET(value = "alpha/{cca3}")
    @org.jetbrains.annotations.Nullable()
    public abstract java.lang.Object getCountryDetail(@retrofit2.http.Path(value = "cca3")
    @org.jetbrains.annotations.NotNull()
    java.lang.String cca3, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.util.List<dev.gustavo.countries.data.remote.model.CountryRemote>> $completion);
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 3, xi = 48)
    public static final class DefaultImpls {
    }
}