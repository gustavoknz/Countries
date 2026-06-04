package dev.gustavo.countries.feature.list;

@kotlin.Metadata(mv = {1, 9, 0}, k = 2, xi = 48, d1 = {"\u00008\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\u001a<\u0010\u0000\u001a\u00020\u00012\f\u0010\u0002\u001a\b\u0012\u0004\u0012\u00020\u00040\u00032\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u00062\u0006\u0010\b\u001a\u00020\t2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a(\u0010\f\u001a\u00020\u00012\u0006\u0010\r\u001a\u00020\u00042\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00010\u000f2\b\b\u0002\u0010\n\u001a\u00020\u000bH\u0003\u001a&\u0010\u0010\u001a\u00020\u00012\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u00020\u0007\u0012\u0004\u0012\u00020\u00010\u00062\b\b\u0002\u0010\u0011\u001a\u00020\u0012H\u0007\u00a8\u0006\u0013"}, d2 = {"CountriesGrid", "", "countries", "", "Ldev/gustavo/countries/domain/model/Country;", "onCountryClick", "Lkotlin/Function1;", "", "contentPadding", "Landroidx/compose/foundation/layout/PaddingValues;", "modifier", "Landroidx/compose/ui/Modifier;", "CountryCard", "country", "onClick", "Lkotlin/Function0;", "ListScreen", "viewModel", "Ldev/gustavo/countries/feature/list/ListViewModel;", "list_debug"})
public final class ListScreenKt {
    
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ListScreen(@org.jetbrains.annotations.NotNull()
    kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCountryClick, @org.jetbrains.annotations.NotNull()
    dev.gustavo.countries.feature.list.ListViewModel viewModel) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CountriesGrid(java.util.List<dev.gustavo.countries.domain.model.Country> countries, kotlin.jvm.functions.Function1<? super java.lang.String, kotlin.Unit> onCountryClick, androidx.compose.foundation.layout.PaddingValues contentPadding, androidx.compose.ui.Modifier modifier) {
    }
    
    @androidx.compose.runtime.Composable()
    private static final void CountryCard(dev.gustavo.countries.domain.model.Country country, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, androidx.compose.ui.Modifier modifier) {
    }
}