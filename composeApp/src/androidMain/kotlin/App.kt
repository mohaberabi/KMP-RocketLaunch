import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import cmpspacex.composeapp.generated.resources.Res
import cmpspacex.composeapp.generated.resources.compose_multiplatform
import com.jetbrains.spacetutorial.RocketLaunch
import com.jetbrains.spacetutorial.RocketLaunchViewModel
import com.jetbrains.spacetutorial.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class)
@Composable
@Preview
fun App() {
    AppTheme {

        val viewModel = koinViewModel<RocketLaunchViewModel>()
        val state by remember { viewModel.state }
        val pullToRefreshState = rememberPullToRefreshState()

        if (pullToRefreshState.isRefreshing) {
            viewModel.loadLaunches()
            pullToRefreshState.endRefresh()
        }


        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text("SpaceX Launches")
                    },
                )
            }
        ) { padding ->

            Box(
                modifier = Modifier
                    .nestedScroll(pullToRefreshState.nestedScrollConnection)
                    .fillMaxSize().padding(padding)
            ) {
                if (state.isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Loading...")
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn {

                        items(state.launches) { rocket ->

                            Column {
                                Text(
                                    "${rocket.missionName} - ${rocket.launchYear}",
                                    style = MaterialTheme.typography.headlineSmall.copy(color = Color.Black)
                                )
                                Spacer(Modifier.height(8.dp))

                                Text(
                                    text = if (rocket.launchSuccess == true) "Successful" else "Unsuccessful",
                                    color = if (rocket.launchSuccess == true) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                )
                                Spacer(Modifier.height(8.dp))
                                val details = rocket.details
                                if (details?.isNotBlank() == true) {
                                    Text(
                                        text = details
                                    )
                                }
                                HorizontalDivider()
                            }
                        }
                    }
                }
                PullToRefreshContainer(
                    state = pullToRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

        }
    }
}