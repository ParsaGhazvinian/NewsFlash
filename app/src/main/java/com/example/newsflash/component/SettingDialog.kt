package com.example.newsflash.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.newsflash.data.datastore.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun SettingDialog(
    isDarkMode: Boolean
) {
    var isToggled by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current
    val dataStore = remember { DataStoreManager(context) }

    IconButton(
        onClick = { isToggled = !isToggled },
        modifier = Modifier.width(32.dp).size(26.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Settings,
            contentDescription = "Setting",
            tint = MaterialTheme.colorScheme.onBackground,

        )
    }
    if (isToggled) {
        Dialog(onDismissRequest = { isToggled = false }) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 16.dp
                ),

                ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Setting",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                    HorizontalDivider(Modifier, DividerDefaults.Thickness, DividerDefaults.color)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                    ) {
                        Text(
                            text = "Dark Mode:",
                            fontSize = 22.sp,
                            modifier = Modifier
                                .padding(8.dp)
                                .weight(2f),
                        )
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = { value ->
                                CoroutineScope(Dispatchers.IO).launch {
                                    dataStore.saveDarkMode(value)
                                }
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
//                    HorizontalDivider(Modifier.width(200.dp).align(Alignment.CenterHorizontally), DividerDefaults.Thickness, DividerDefaults.color)

                }


            }
        }
    }
}