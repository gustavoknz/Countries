package dev.gustavo.countries.feature.detail;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000J\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0007\u0018\u00002\u00020\u0001B\u000f\b\u0007\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u0010\u0010\u0013\u001a\u00020\u00142\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0014H\u0002J\u000e\u0010\u0018\u001a\u00020\u00142\u0006\u0010\u0019\u001a\u00020\u001aR\u0014\u0010\u0005\u001a\b\u0012\u0004\u0012\u00020\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0014\u0010\b\u001a\b\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\u00070\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0017\u0010\u000f\u001a\b\u0012\u0004\u0012\u00020\n0\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012\u00a8\u0006\u001b"}, d2 = {"Ldev/gustavo/countries/feature/detail/DetailViewModel;", "Landroidx/lifecycle/ViewModel;", "getCountryDetailUseCase", "Ldev/gustavo/countries/domain/usecase/GetCountryDetailUseCase;", "(Ldev/gustavo/countries/domain/usecase/GetCountryDetailUseCase;)V", "_events", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Ldev/gustavo/countries/feature/detail/DetailEvent;", "_viewState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Ldev/gustavo/countries/feature/detail/DetailViewState;", "events", "Lkotlinx/coroutines/flow/SharedFlow;", "getEvents", "()Lkotlinx/coroutines/flow/SharedFlow;", "viewState", "Lkotlinx/coroutines/flow/StateFlow;", "getViewState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadDetail", "", "cca3", "", "navigateBack", "onAction", "action", "Ldev/gustavo/countries/feature/detail/DetailAction;", "detail_debug"})
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class DetailViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final dev.gustavo.countries.domain.usecase.GetCountryDetailUseCase getCountryDetailUseCase = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<dev.gustavo.countries.feature.detail.DetailViewState> _viewState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<dev.gustavo.countries.feature.detail.DetailViewState> viewState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<dev.gustavo.countries.feature.detail.DetailEvent> _events = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.SharedFlow<dev.gustavo.countries.feature.detail.DetailEvent> events = null;
    
    @javax.inject.Inject()
    public DetailViewModel(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.domain.usecase.GetCountryDetailUseCase getCountryDetailUseCase) {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<dev.gustavo.countries.feature.detail.DetailViewState> getViewState() {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.SharedFlow<dev.gustavo.countries.feature.detail.DetailEvent> getEvents() {
        return null;
    }
    
    public final void onAction(@org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.feature.detail.DetailAction action) {
    }
    
    private final void loadDetail(java.lang.String cca3) {
    }
    
    private final void navigateBack() {
    }
}