package dev.gustavo.countries

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import dev.gustavo.countries.core.ui.theme.CountriesTheme
import dev.gustavo.countries.feature.detail.DetailScreen
import dev.gustavo.countries.feature.list.ListScreen

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CountriesTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "list"
                ) {
                    composable("list") {
                        ListScreen(
                            onCountryClick = { countryCode ->
                                navController.navigate("detail/$countryCode")
                            }
                        )
                    }
                    composable(
                        route = "detail/{countryCode}",
                        arguments = listOf(navArgument("countryCode") { type = NavType.StringType })
                    ) { backStackEntry ->
                        DetailScreen(
                            countryCode = backStackEntry.arguments?.getString("countryCode").orEmpty(),
                            onBack = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}
