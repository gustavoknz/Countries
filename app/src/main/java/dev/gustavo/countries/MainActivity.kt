package dev.gustavo.countries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
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
                            duration = SnackbarDuration.Indefinite,
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            viewModel.dismissSnackbar()
                        }
                    } else {
                        snackbarHostState.currentSnackbarData?.dismiss()
                    }
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(snackbarHostState) { data ->
                            Snackbar(
                                snackbarData = data,
                                containerColor = LightRed,
                                contentColor = DarkRed,
                                actionColor = DarkRed
                            )
                        }
                    },
                ) { innerPadding ->
                    val navController = rememberNavController()
                    SharedTransitionLayout {
                        NavHost(
                            navController = navController,
                            startDestination = Routes.List,
                            modifier = Modifier.padding(innerPadding)
                        ) {
                            composable<Routes.List> {
                                ListRoute(
                                    onCountryClick = { countryCode: String ->
                                        navController.navigate(Routes.Detail(countryCode))
                                    },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedContentScope = this@composable
                                )
                            }
                            composable<Routes.Detail> { backStackEntry ->
                                val detail: Routes.Detail = backStackEntry.toRoute()
                                DetailRoute(
                                    countryCode = detail.countryCode,
                                    onBack = navController::popBackStack,
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedContentScope = this@composable
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
