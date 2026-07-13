package dev.gustavo.countries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import dev.gustavo.countries.core.common.navigation.Routes
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.core.ui.theme.DarkRed
import dev.gustavo.countries.core.ui.theme.LightRed
import dev.gustavo.countries.feature.detail.DetailRoute
import dev.gustavo.countries.feature.list.ListRoute

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CountriesTheme {
                val viewModel: MainViewModel = hiltViewModel()
                val showConnectivitySnackbar by viewModel.showConnectivitySnackbar.collectAsStateWithLifecycle()
                val snackbarHostState = remember { SnackbarHostState() }
                val offlineMessage = stringResource(R.string.no_internet_connection)
                val dismissLabel = stringResource(R.string.dismiss)

                LaunchedEffect(showConnectivitySnackbar) {
                    if (showConnectivitySnackbar) {
                        val result = snackbarHostState.showSnackbar(
                            message = offlineMessage,
                            actionLabel = dismissLabel,
                            duration = SnackbarDuration.Indefinite
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.dismissSnackbar()
                        }
                    } else {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    SharedTransitionLayout {
                        Box(modifier = Modifier.fillMaxSize()) {
                            NavHost(
                                navController = navController,
                                startDestination = Routes.List
                            ) {
                                composable<Routes.List> { backStackEntry ->
                                    ListRoute(
                                        onCountryClick = { countryCode, flagUrl ->
                                            if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                                                navController.navigate(Routes.Detail(countryCode, flagUrl))
                                            }
                                        },
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedContentScope = this@composable
                                    )
                                }
                                composable<Routes.Detail> { backStackEntry ->
                                    val detail: Routes.Detail = backStackEntry.toRoute()
                                    DetailRoute(
                                        countryCode = detail.countryCode,
                                        flagUrl = detail.flagUrl,
                                        onBack = {
                                            if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                                                navController.popBackStack()
                                            }
                                        },
                                        onCountryClick = { cca3 ->
                                            if (backStackEntry.lifecycle.currentState == Lifecycle.State.RESUMED) {
                                                navController.navigate(Routes.Detail(cca3))
                                            }
                                        },
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedContentScope = this@composable
                                    )
                                }
                            }

                            SnackbarHost(
                                hostState = snackbarHostState,
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .windowInsetsPadding(WindowInsets.navigationBars)
                            ) { data ->
                                Snackbar(
                                    snackbarData = data,
                                    containerColor = LightRed,
                                    contentColor = DarkRed,
                                    actionColor = DarkRed
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
