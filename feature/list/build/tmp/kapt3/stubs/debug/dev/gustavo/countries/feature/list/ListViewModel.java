package dev.gustavo.countries.feature.list;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000T\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\b\u0007\u0018\u00002\u00020\u0001B\u0017\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0006J\b\u0010\u0015\u001a\u00020\u0016H\u0002J\u0010\u0010\u0017\u001a\u00020\u00162\u0006\u0010\u0018\u001a\u00020\u0019H\u0002J\u000e\u0010\u001a\u001a\u00020\u00162\u0006\u0010\u001b\u001a\u00020\u001cJ\u0010\u0010\u001d\u001a\u00020\u00162\u0006\u0010\u001e\u001a\u00020\u0019H\u0002R\u0014\u0010\u0007\u001a\b\u0012\u0004\u0012\u00020\t0\bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\n\u001a\b\u0012\u0004\u0012\u00020\f0\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\r\u001a\b\u0012\u0004\u0012\u00020\t0\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000f\u0010\u0010R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\f0\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\u001f"}, d2 = {"Ldev/gustavo/countries/feature/list/ListViewModel;", "Landroidx/lifecycle/ViewModel;", "getCountriesUseCase", "Ldev/gustavo/countries/domain/usecase/GetCountriesUseCase;", "searchCountriesUseCase", "Ldev/gustavo/countries/domain/usecase/SearchCountriesUseCase;", "(Ldev/gustavo/countries/domain/usecase/GetCountriesUseCase;Ldev/gustavo/countries/domain/usecase/SearchCountriesUseCase;)V", "_events", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Ldev/gustavo/countries/feature/list/ListEvent;", "_viewState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Ldev/gustavo/countries/feature/list/ListViewState;", "events", "Lkotlinx/coroutines/flow/SharedFlow;", "getEvents", "()Lkotlinx/coroutines/flow/SharedFlow;", "viewState", "Lkotlinx/coroutines/flow/StateFlow;", "getViewState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadCountries", "", "navigateToDetail", "cca3", "", "onAction", "action", "Ldev/gustavo/countries/feature/list/ListAction;", "search", "query", "list_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ListViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.domain.usecase.GetCountriesUseCase getCountriesUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.domain.usecase.SearchCountriesUseCase searchCountriesUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<dev.gustavo.countries.feature.list.ListViewState> _viewState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<dev.gustavo.countries.feature.list.ListViewState> viewState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<dev.gustavo.countries.feature.list.ListEvent> _events = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<dev.gustavo.countries.feature.list.ListEvent> events = null;
    
    @javax.inject.Inject()
    public ListViewModel(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.domain.usecase.GetCountriesUseCase getCountriesUseCase, @org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.domain.usecase.SearchCountriesUseCase searchCountriesUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<dev.gustavo.countries.feature.list.ListViewState> getViewState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<dev.gustavo.countries.feature.list.ListEvent> getEvents() {
        return null;
    }
    
    public final void onAction(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.feature.list.ListAction action) {
    }
    
    private final void loadCountries() {
    }
    
    private final void search(java.lang.String query) {
    }
    
    private final void navigateToDetail(java.lang.String cca3) {
    }
}