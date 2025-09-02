package com.example.appsemana1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateBack: () -> Unit
) {
    // Estados para los diferentes componentes
    var textFieldValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    var multilineText by remember { mutableStateOf("") }

    // Checkbox states
    var checkbox1 by remember { mutableStateOf(false) }
    var checkbox2 by remember { mutableStateOf(true) }
    var checkbox3 by remember { mutableStateOf(false) }

    // Radio button state
    var selectedRadioOption by remember { mutableStateOf("opcion1") }

    // Dropdown (ComboBox) state
    var expanded by remember { mutableStateOf(false) }
    var selectedDropdownItem by remember { mutableStateOf("Selecciona una opción") }
    val dropdownItems = listOf("Opción 1", "Opción 2", "Opción 3", "Opción 4")

    // Switch state
    var switchState by remember { mutableStateOf(false) }

    // Slider state
    var sliderValue by remember { mutableStateOf(0.5f) }

    // Tab state
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Todos los Componentes UI") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // SECCIÓN 1: INPUTS / TEXT FIELDS
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "1. INPUTS (Text Fields)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // TextField básico
                        TextField(
                            value = textFieldValue,
                            onValueChange = { textFieldValue = it },
                            label = { Text("TextField Básico") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // OutlinedTextField
                        OutlinedTextField(
                            value = passwordValue,
                            onValueChange = { passwordValue = it },
                            label = { Text("OutlinedTextField (Password)") },
                            leadingIcon = { Icon(Icons.Default.Lock, null) },
                            trailingIcon = { Icon(Icons.Default.Visibility, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        // TextField multilínea
                        OutlinedTextField(
                            value = multilineText,
                            onValueChange = { multilineText = it },
                            label = { Text("TextField Multilínea") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            maxLines = 4
                        )
                    }
                }
            }

            // SECCIÓN 2: BOTONES
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "2. BOTONES",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Button filled
                            Button(onClick = { }) {
                                Text("Button")
                            }

                            // Elevated Button
                            ElevatedButton(onClick = { }) {
                                Text("Elevated")
                            }

                            // Tonal Button
                            FilledTonalButton(onClick = { }) {
                                Text("Tonal")
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Outlined Button
                            OutlinedButton(onClick = { }) {
                                Text("Outlined")
                            }

                            // Text Button
                            TextButton(onClick = { }) {
                                Text("Text Button")
                            }

                            // Icon Button
                            IconButton(onClick = { }) {
                                Icon(Icons.Default.Favorite, "Favorite")
                            }
                        }

                        // Extended FAB
                        ExtendedFloatingActionButton(
                            onClick = { },
                            text = { Text("Extended FAB") },
                            icon = { Icon(Icons.Default.Add, null) }
                        )
                    }
                }
            }

            // SECCIÓN 3: CHECKBOX
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "3. CHECKBOX / CHECKLIST",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = checkbox1,
                                onCheckedChange = { checkbox1 = it }
                            )
                            Text("Opción 1", modifier = Modifier.padding(start = 8.dp))
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = checkbox2,
                                onCheckedChange = { checkbox2 = it }
                            )
                            Text("Opción 2 (Marcada)", modifier = Modifier.padding(start = 8.dp))
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Checkbox(
                                checked = checkbox3,
                                onCheckedChange = { checkbox3 = it }
                            )
                            Text("Opción 3", modifier = Modifier.padding(start = 8.dp))
                        }
                    }
                }
            }

            // SECCIÓN 4: RADIO BUTTONS
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "4. RADIO BUTTONS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        val radioOptions = listOf("opcion1", "opcion2", "opcion3")
                        val radioLabels = listOf("Radio 1", "Radio 2", "Radio 3")

                        radioOptions.forEachIndexed { index, option ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedRadioOption = option }
                            ) {
                                RadioButton(
                                    selected = (selectedRadioOption == option),
                                    onClick = { selectedRadioOption = option }
                                )
                                Text(
                                    text = radioLabels[index],
                                    modifier = Modifier.padding(start = 8.dp)
                                )
                            }
                        }
                    }
                }
            }

            // SECCIÓN 5: DROPDOWN (COMBO BOX)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "5. COMBO BOX (Dropdown Menu)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded }
                        ) {
                            OutlinedTextField(
                                value = selectedDropdownItem,
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Selecciona una opción") },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .menuAnchor()
                            )

                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false }
                            ) {
                                dropdownItems.forEach { item ->
                                    DropdownMenuItem(
                                        text = { Text(item) },
                                        onClick = {
                                            selectedDropdownItem = item
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // SECCIÓN 6: TABLA (Table-like layout)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "6. TABLA",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Encabezados de tabla
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primaryContainer)
                                .padding(8.dp)
                        ) {
                            Text(
                                "ID",
                                modifier = Modifier.weight(1f),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Nombre",
                                modifier = Modifier.weight(2f),
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Estado",
                                modifier = Modifier.weight(1.5f),
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Filas de tabla
                        val tableData = listOf(
                            Triple("1", "Juan Pérez", "Activo"),
                            Triple("2", "María García", "Inactivo"),
                            Triple("3", "Carlos López", "Activo"),
                            Triple("4", "Ana Martínez", "Pendiente")
                        )

                        tableData.forEach { (id, name, status) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .border(
                                        0.5.dp,
                                        MaterialTheme.colorScheme.outline
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(id, modifier = Modifier.weight(1f))
                                Text(name, modifier = Modifier.weight(2f))
                                Text(status, modifier = Modifier.weight(1.5f))
                            }
                        }
                    }
                }
            }

            // SECCIÓN 7: GRILLA (Grid)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "7. GRILLA (Grid)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        val gridItems = (1..6).map { "Item $it" }

                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            modifier = Modifier.height(200.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(gridItems) { item ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(60.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.secondaryContainer
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(item)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // SECCIÓN 8: VÍNCULOS (Links)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "8. VÍNCULOS (Links)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Link simple
                        Text(
                            text = "Link simple clickeable",
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { /* Acción */ },
                            style = MaterialTheme.typography.bodyLarge.copy(
                                textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                            )
                        )

                        // Link con icono
                        Row(
                            modifier = Modifier.clickable { /* Acción */ },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.OpenInNew,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Abrir enlace externo",
                                color = MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    textDecoration = androidx.compose.ui.text.style.TextDecoration.Underline
                                )
                            )
                        }

                        // TextButton como link
                        TextButton(onClick = { /* Acción */ }) {
                            Text("Link como TextButton")
                        }
                    }
                }
            }

            // SECCIÓN 9: TEXTOS (Diferentes estilos)
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            "9. TEXTOS (Diferentes estilos)",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Text(
                            "Display Large",
                            style = MaterialTheme.typography.displayLarge
                        )

                        Text(
                            "Headline Medium",
                            style = MaterialTheme.typography.headlineMedium
                        )

                        Text(
                            "Title Large",
                            style = MaterialTheme.typography.titleLarge
                        )

                        Text(
                            "Body Large - Lorem ipsum dolor sit amet",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Text(
                            "Label Medium",
                            style = MaterialTheme.typography.labelMedium
                        )

                        Text(
                            "Texto en Negrita",
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            "Texto en Cursiva",
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )

                        Text(
                            "Texto de Color Personalizado",
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            // SECCIÓN 10: COMPONENTES ADICIONALES
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "10. COMPONENTES ADICIONALES",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary
                        )

                        // Switch
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Switch:", modifier = Modifier.weight(1f))
                            Switch(
                                checked = switchState,
                                onCheckedChange = { switchState = it }
                            )
                        }

                        // Slider
                        Column {
                            Text("Slider: ${(sliderValue * 100).toInt()}%")
                            Slider(
                                value = sliderValue,
                                onValueChange = { sliderValue = it },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        // Chips
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AssistChip(
                                onClick = { },
                                label = { Text("Assist Chip") },
                                leadingIcon = {
                                    Icon(Icons.Default.Info, null, Modifier.size(16.dp))
                                }
                            )

                            FilterChip(
                                selected = true,
                                onClick = { },
                                label = { Text("Filter Chip") }
                            )

                            SuggestionChip(
                                onClick = { },
                                label = { Text("Suggestion") }
                            )
                        }

                        // Progress Indicators
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator()
                            LinearProgressIndicator(
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            // SECCIÓN 11: TABS
            item {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            "11. TABS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        TabRow(selectedTabIndex = selectedTab) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = { Text("Tab 1") }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = { Text("Tab 2") }
                            )
                            Tab(
                                selected = selectedTab == 2,
                                onClick = { selectedTab = 2 },
                                text = { Text("Tab 3") }
                            )
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(top = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Contenido del Tab ${selectedTab + 1}")
                        }
                    }
                }
            }
        }
    }
}