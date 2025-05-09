package dev.luanramos.thelightningnodesrank.presentation.main

import dev.luanramos.thelightningnodesrank.R
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import dev.luanramos.thelightningnodesrank.domain.model.Node
import dev.luanramos.thelightningnodesrank.domain.model.TranslatedField
import dev.luanramos.thelightningnodesrank.presentation.theme.TheLightningNodesRankTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import dev.luanramos.thelightningnodesrank.presentation.ui.component.CustomCircularProgressBar

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
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {}
            )
        },
        content = { innerPadding ->
            if (shouldShowProgress) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    CustomCircularProgressBar(
                        modifier = Modifier
                            .size(100.dp)
                            .padding(16.dp)
                            .align(Alignment.Center)
                    )
                }
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                nodeList?.let {
                    items(it) { node ->
                        NodeListItem(node){ node ->
                            //TODO: Do Any action after the user clicks in a node item
                            Toast.makeText(context, node.alias, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

            }
        }
    )
}

@Composable
fun NodeListItem(node: Node, modifier: Modifier = Modifier, onClick:(Node) -> Unit) {
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
        Row(modifier = Modifier.padding(8.dp)) {
            Text(node.alias, style = MaterialTheme.typography.titleMedium)
        }
        Row(modifier = Modifier.padding(8.dp)) {
            Column {
                Text("Channels: ${node.channels}")
                Text("Capacity: ${node.capacity}")
                Text("First Seen: ${formatTimestamp(node.firstSeen)}")
            }
            Spacer(Modifier.width(8.dp))
            Column {
                Text("Updated At: ${formatTimestamp(node.updatedAt)}")

                node.city?.en?.let {
                    Text("City: $it")
                }

                node.country?.en?.let {
                    Text("Country: $it")
                }
            }
        }
        Row(modifier = Modifier.padding(8.dp)) {
            Text("Public Key: ${node.publicKey}", style = MaterialTheme.typography.bodySmall)
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