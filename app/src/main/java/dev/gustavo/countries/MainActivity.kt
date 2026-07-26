package dev.gustavo.countries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffold
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import dev.gustavo.countries.core.common.navigation.Routes
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.theme.DarkRed
import dev.gustavo.countries.core.ui.theme.LightRed
import dev.gustavo.countries.feature.detail.DetailRoute
import dev.gustavo.countries.feature.list.ListRoute
import kotlinx.coroutines.launch
import dev.gustavo.countries.core.ui.R as UiR

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CountriesTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val showConnectivitySnackbar by viewModel.showConnectivitySnackbar.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }
                val offlineMessage = stringResource(UiR.string.common_no_internet_short)
                val dismissLabel = stringResource(UiR.string.common_dismiss)

                LaunchedEffect(showConnectivitySnackbar) {
                    if (showConnectivitySnackbar) {
                        val result =
                            snackbarHostState.showSnackbar(
                                message = offlineMessage,
                                actionLabel = dismissLabel,
                                duration = SnackbarDuration.Indefinite,
                            )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.dismissSnackbar()
                        }
                    } else {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }

                Surface(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .semantics {
                                testTagsAsResourceId = true
                            },
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SharedTransitionLayout {
                        val navigator = rememberListDetailPaneScaffoldNavigator<Routes.Detail>()
                        val scope = rememberCoroutineScope()

                        BackHandler(navigator.canNavigateBack()) {
                            scope.launch {
                                navigator.navigateBack()
                            }
                        }

                        ListDetailPaneScaffold(
                            directive = navigator.scaffoldDirective,
                            value = navigator.scaffoldValue,
                            listPane = {
                                AnimatedPane {
                                    ListRoute(
                                        onCountryClick = { countryCode, flagUrl ->
                                            scope.launch {
                                                navigator.navigateTo(
                                                    ListDetailPaneScaffoldRole.Detail,
                                                    Routes.Detail(countryCode, flagUrl),
                                                )
                                            }
                                        },
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedContentScope = this,
                                    )
                                }
                            },
                            detailPane = {
                                AnimatedPane {
                                    val detailRoute = navigator.currentDestination?.contentKey
                                    if (detailRoute != null) {
                                        DetailRoute(
                                            countryCode = detailRoute.countryCode,
                                            flagUrl = detailRoute.flagUrl,
                                            onBack = {
                                                scope.launch {
                                                    navigator.navigateBack()
                                                }
                                            },
                                            onCountryClick = { countryCode ->
                                                scope.launch {
                                                    navigator.navigateTo(
                                                        ListDetailPaneScaffoldRole.Detail,
                                                        Routes.Detail(countryCode),
                                                    )
                                                }
                                            },
                                            sharedTransitionScope = this@SharedTransitionLayout,
                                            animatedContentScope = this,
                                            showTopAppBar =
                                                navigator.scaffoldValue[ListDetailPaneScaffoldRole.List] ==
                                                    PaneAdaptedValue.Hidden,
                                        )
                                    } else {
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center,
                                        ) {
                                            Text(
                                                text = stringResource(UiR.string.common_select_country_prompt),
                                                style = MaterialTheme.typography.bodyLarge,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            )
                                        }
                                    }
                                }
                            },
                        )

                        Box(modifier = Modifier.fillMaxSize()) {
                            SnackbarHost(
                                hostState = snackbarHostState,
                                modifier =
                                    Modifier
                                        .align(Alignment.BottomCenter)
                                        .windowInsetsPadding(WindowInsets.navigationBars),
                            ) { data ->
                                Snackbar(
                                    snackbarData = data,
                                    containerColor = LightRed,
                                    contentColor = DarkRed,
                                    actionColor = DarkRed,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
