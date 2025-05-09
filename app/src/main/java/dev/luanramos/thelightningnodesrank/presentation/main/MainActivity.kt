package dev.luanramos.thelightningnodesrank.presentation.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import dev.luanramos.thelightningnodesrank.R
import dev.luanramos.thelightningnodesrank.domain.model.Node
import dev.luanramos.thelightningnodesrank.domain.model.TranslatedField
import dev.luanramos.thelightningnodesrank.presentation.theme.TheLightningNodesRankTheme
import dev.luanramos.thelightningnodesrank.presentation.ui.component.CustomCircularProgressBar
import dev.luanramos.thelightningnodesrank.utils.copyToClipboard
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            TheLightningNodesRankTheme {
                MainScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel){

    val nodeList by viewModel.nodesData.observeAsState()
    var shouldShowProgress by remember { mutableStateOf(true) }
    val context = LocalContext.current

    LaunchedEffect(nodeList) {
        if (nodeList != null) {
            shouldShowProgress = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        shouldShowProgress = true
                        viewModel.clearNodeList()
                        viewModel.loadNodesList()
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_update),
                            contentDescription = "Update"
                        )
                    }
                }
            )
        },
        content = { innerPadding ->
            val modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

            when {
                shouldShowProgress -> {
                    Box(modifier = modifier) {
                        CustomCircularProgressBar(
                            modifier = Modifier
                                .size(100.dp)
                                .padding(16.dp)
                                .align(Alignment.Center)
                        )
                    }
                }

                nodeList.isNullOrEmpty() -> {
                    Box(modifier = modifier, contentAlignment = Alignment.Center) {
                        Text(
                            text = "No data available",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                else -> {
                    LazyColumn(modifier = modifier) {
                        nodeList?.let {
                            items(it) { node ->
                                NodeListItem(node) { clickedNode ->
                                    context.copyToClipboard("Copy public key", node.publicKey)
                                }
                            }
                        }
                    }
                }
            }
        }

    )
}

@Composable
fun NodeListItem(node: Node, modifier: Modifier = Modifier, onClick:(Node) -> Unit) {

    val nodeCity = node.city?.ptBR ?: node.city?.en
    val nodeCountry = node.country?.ptBR ?: node.country?.en

    Card(
        modifier = modifier
            .padding(8.dp)
            .clickable { onClick(node) }
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = node.alias,
                style = MaterialTheme.typography.titleMedium
            )

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("Capacity: ${satsToBitcoin(node.capacity)}")
                    Text("Channels: ${node.channels}")
                    Text("Country: $nodeCountry")
                }

                VerticalDivider(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(1.dp)
                        .padding(horizontal = 8.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text("City: $nodeCity")
                    Text("First Seen: ${formatTimestamp(node.firstSeen)}")
                    Text("Updated At: ${formatTimestamp(node.updatedAt)}")
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text(
                text = "Public Key: ${node.publicKey}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

    }
}

fun formatTimestamp(timestamp: Long): String {
    val date = Date(timestamp * 1000)
    val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return format.format(date)
}

@Preview(showBackground = true)
@Composable
fun NodeListItemPreview() {
    val sampleNode = Node(
        publicKey = "03864ef025fde8fb587d989186ce6a4a186895ee44a926bfc370e2c366597a3f8f",
        alias = "ACINQ",
        channels = 2908,
        capacity = 36010516297,
        firstSeen = 1522941222,
        updatedAt = 1661274935,
        city = TranslatedField(
            de = "Berlin",
            en = "Berlin",
            es = "Berlín",
            fr = "Berlin",
            ja = "ベルリン",
            ptBR = "Berlim",
            ru = "Берлин",
            zhCN = "柏林"
        ),
        country = TranslatedField(
            de = "Deutschland",
            en = "Germany",
            es = "Alemania",
            fr = "Allemagne",
            ja = "ドイツ",
            ptBR = "Alemanha",
            ru = "Германия",
            zhCN = "德国"
        )
    )

    NodeListItem(node = sampleNode, onClick = {})
}

fun satsToBitcoin(sats: Long): String {
    val btc = sats / 100_000_000.0
    return String.format(Locale.getDefault(), "%.8f BTC", btc)
}