package com.example.appsemana1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsemana1.ui.theme.AccessibilityViewModel
import com.example.appsemana1.ui.theme.LocalAccessibilitySettings
import com.example.appsemana1.ui.theme.rememberAccessibilityManager

enum class MealType { DESAYUNO, ALMUERZO, CENA }

data class Ingredient(val name: String, val qty: String)
data class Recipe(
    val id: String,
    val title: String,
    val minutes: Int,
    val type: MealType,
    val tags: List<String> = emptyList(),
    val ingredients: List<Ingredient> = emptyList()
)
data class DayPlan(val day: String, val breakfast: Recipe? = null, val lunch: Recipe? = null, val dinner: Recipe? = null)
data class WeekPlan(val days: List<DayPlan>)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    accessibilityViewModel: AccessibilityViewModel
) {
    val settings = LocalAccessibilitySettings.current
    val multiplier = settings.fontSize.multiplier
    val manager = rememberAccessibilityManager()
    val context = LocalContext.current
    var showAccDialog by remember { mutableStateOf(false) }

    var tab by remember { mutableStateOf(0) }
    var week by remember {
        mutableStateOf(
            WeekPlan(listOf("Lunes","Martes","Miércoles","Jueves","Viernes","Sábado","Domingo").map { DayPlan(it) })
        )
    }
    val recipes = remember {
        listOf(
            Recipe("r1","Avena con fruta",10,MealType.DESAYUNO, listOf("Rápido"), listOf(Ingredient("Avena","1/2 tz"), Ingredient("Leche","1 tz"))),
            Recipe("r2","Panqueques de avena",15,MealType.DESAYUNO, listOf("Económico"), listOf(Ingredient("Avena","1 tz"), Ingredient("Huevo","1 un"))),
            Recipe("r3","Pollo con ensalada",25,MealType.ALMUERZO, listOf("Rápido"), listOf(Ingredient("Pollo","1 pechuga"), Ingredient("Lechuga","2 tz"))),
            Recipe("r4","Porotos con rienda",45,MealType.ALMUERZO, listOf("Económico"), listOf(Ingredient("Porotos","1 tz"), Ingredient("Tallarin","1 tz"))),
            Recipe("r5","Tortilla de verduras",20,MealType.CENA, listOf("Vegetariano"), listOf(Ingredient("Huevo","3 un"), Ingredient("Cebolla","1/2 un"))),
            Recipe("r6","Sopa de verduras",30,MealType.CENA, listOf("Ligero"), listOf(Ingredient("Zanahoria","1 un"), Ingredient("Papa","1 un")))
        )
    }
    val shopping by derivedStateOf {
        val map = linkedMapOf<String, MutableList<String>>()
        week.days.forEach { d ->
            listOfNotNull(d.breakfast, d.lunch, d.dinner).forEach { r ->
                r.ingredients.forEach { i -> map.getOrPut(i.name) { mutableListOf() }.add(i.qty) }
            }
        }
        map.map { (k, v) -> Ingredient(k, v.joinToString(" + ")) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Minuta Semanal", fontWeight = FontWeight.Bold, fontSize = (20 * multiplier).sp) },
                actions = {
                    // Botón accesibilidad
                    manager.AccessibilityIconButton(
                        onAccessibilityClick = { showAccDialog = true }
                    )
                    // Logout
                    IconButton(onClick = onLogout) {
                        Icon(Icons.Default.ExitToApp, contentDescription = "Cerrar sesión")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(selected = tab == 0, onClick = { tab = 0 }, icon = { Icon(Icons.Default.CalendarToday, null) }, label = { Text("Semana") })
                NavigationBarItem(selected = tab == 1, onClick = { tab = 1 }, icon = { Icon(Icons.Default.Search, null) }, label = { Text("Recetas") })
                NavigationBarItem(selected = tab == 2, onClick = { tab = 2 }, icon = { Icon(Icons.Default.ListAlt, null) }, label = { Text("Lista") })
                NavigationBarItem(selected = tab == 3, onClick = { tab = 3 }, icon = { Icon(Icons.Default.Person, null) }, label = { Text("Perfil") })
            }
        },
        floatingActionButton = {
            if (tab == 0) {
                ExtendedFloatingActionButton(
                    onClick = {
                        val b = recipes.filter { it.type == MealType.DESAYUNO }
                        val l = recipes.filter { it.type == MealType.ALMUERZO }
                        val c = recipes.filter { it.type == MealType.CENA }
                        week = week.copy(
                            days = week.days.mapIndexed { i, d ->
                                d.copy(
                                    breakfast = b.getOrNull(i % maxOf(1, b.size)),
                                    lunch = l.getOrNull(i % maxOf(1, l.size)),
                                    dinner = c.getOrNull(i % maxOf(1, c.size))
                                )
                            }
                        )
                    },
                    icon = { Icon(Icons.Default.AutoAwesome, null) },
                    text = { Text("Generar semana") }
                )
            }
        }
    ) { pv ->
        when (tab) {
            0 -> WeekTab(pv, week, recipes) { week = it }
            1 -> RecipesTab(pv, recipes)
            2 -> ShoppingTab(pv, shopping)
            3 -> ProfileTab(pv)
        }
    }

    if (showAccDialog) {
        manager.AccessibilityDialog(
            currentSettings = settings,
            onSettingsChanged = { new -> accessibilityViewModel.updateSettings(context, new) },
            onDismiss = { showAccDialog = false }
        )
    }
}

@Composable
fun WeekTab(pv: PaddingValues, plan: WeekPlan, recipes: List<Recipe>, onChange: (WeekPlan) -> Unit) {
    val multiplier = LocalAccessibilitySettings.current.fontSize.multiplier
    var picker by remember { mutableStateOf<Triple<Int, MealType, Boolean>?>(null) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Elige tus recetas por día", fontSize = (20 * multiplier).sp, fontWeight = FontWeight.Bold)
        }
        items(plan.days.size) { idx ->
            val d = plan.days[idx]
            Card(shape = RoundedCornerShape(16.dp)) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(d.day, fontWeight = FontWeight.Bold, fontSize = (18 * multiplier).sp)
                    Spacer(Modifier.height(10.dp))
                    MealRow("Desayuno", d.breakfast, Icons.Default.WbSunny) { picker = Triple(idx, MealType.DESAYUNO, true) }
                    Spacer(Modifier.height(8.dp))
                    MealRow("Almuerzo", d.lunch, Icons.Default.LunchDining) { picker = Triple(idx, MealType.ALMUERZO, true) }
                    Spacer(Modifier.height(8.dp))
                    MealRow("Cena", d.dinner, Icons.Default.Nightlight) { picker = Triple(idx, MealType.CENA, true) }
                }
            }
        }
    }

    val p = picker
    if (p != null && p.third) {
        val options = recipes.filter { it.type == p.second }
        AlertDialog(
            onDismissRequest = { picker = null },
            title = { Text("Elegir receta") },
            text = {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(options) { r ->
                        ElevatedCard(
                            onClick = {
                                val days = plan.days.toMutableList()
                                val d0 = days[p.first]
                                days[p.first] = when (p.second) {
                                    MealType.DESAYUNO -> d0.copy(breakfast = r)
                                    MealType.ALMUERZO -> d0.copy(lunch = r)
                                    MealType.CENA -> d0.copy(dinner = r)
                                }
                                onChange(plan.copy(days = days))
                                picker = null
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    when (r.type) {
                                        MealType.DESAYUNO -> Icons.Default.WbSunny
                                        MealType.ALMUERZO -> Icons.Default.LunchDining
                                        MealType.CENA -> Icons.Default.Nightlight
                                    },
                                    contentDescription = null
                                )
                                Spacer(Modifier.width(10.dp))
                                Column(Modifier.weight(1f)) {
                                    Text(r.title, fontWeight = FontWeight.SemiBold)
                                    Text("${r.minutes} min • ${r.tags.joinToString()}", fontSize = 12.sp)
                                }
                                Icon(Icons.Default.CheckCircle, contentDescription = null)
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { picker = null }) { Text("Cerrar") } }
        )
    }
}

@Composable
fun MealRow(label: String, recipe: Recipe?, icon: Icons.Filled, onPick: () -> Unit) {
    // Nota: firma de icon param era ImageVector, pero pasamos íconos de Filled
    // Ajustamos tipo:
}

@Composable
fun MealRow(label: String, recipe: Recipe?, icon: androidx.compose.ui.graphics.vector.ImageVector, onPick: () -> Unit) {
    val multiplier = LocalAccessibilitySettings.current.fontSize.multiplier
    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(0.8f), fontWeight = FontWeight.SemiBold, fontSize = (14 * multiplier).sp)
        if (recipe == null) {
            OutlinedButton(onClick = onPick, modifier = Modifier.weight(1.2f)) {
                Icon(Icons.Default.Add, contentDescription = null)
                Spacer(Modifier.width(6.dp))
                Text("Elegir receta")
            }
        } else {
            Column(
                modifier = Modifier
                    .weight(1.2f)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { onPick() }
                    .padding(12.dp)
            ) {
                Text(recipe.title, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("${recipe.minutes} min", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun RecipesTab(pv: PaddingValues, recipes: List<Recipe>) {
    val multiplier = LocalAccessibilitySettings.current.fontSize.multiplier
    var q by remember { mutableStateOf("") }
    var t by remember { mutableStateOf<MealType?>(null) }
    val filtered = recipes.filter { (t == null || it.type == t) && (q.isBlank() || it.title.lowercase().contains(q.lowercase())) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = q,
            onValueChange = { q = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Buscar receta") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true,
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = t == null, onClick = { t = null }, label = { Text("Todos") })
            FilterChip(selected = t == MealType.DESAYUNO, onClick = { t = MealType.DESAYUNO }, label = { Text("Desayuno") })
            FilterChip(selected = t == MealType.ALMUERZO, onClick = { t = MealType.ALMUERZO }, label = { Text("Almuerzo") }) // ← FIX
            FilterChip(selected = t == MealType.CENA, onClick = { t = MealType.CENA }, label = { Text("Cena") })             // ← FIX
        }
        Spacer(Modifier.height(16.dp))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filtered) { r ->
                Card(onClick = {}, shape = RoundedCornerShape(12.dp)) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(r.title, fontWeight = FontWeight.SemiBold, fontSize = (16 * multiplier).sp)
                        Spacer(Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Timer, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("${r.minutes} min", fontSize = 12.sp)
                        }
                        Spacer(Modifier.height(6.dp))
                        Text(r.tags.joinToString(" • "), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingTab(pv: PaddingValues, items: List<Ingredient>) {
    val multiplier = LocalAccessibilitySettings.current.fontSize.multiplier
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
            .padding(16.dp)
    ) {
        Text("Lista de compras", fontSize = (20 * multiplier).sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        if (items.isEmpty()) {
            Text("Agrega recetas para ver los ingredientes.")
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(items) { ing ->
                    Card(shape = RoundedCornerShape(12.dp)) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) { Icon(Icons.Default.ListAlt, contentDescription = null) } // ← ícono seguro
                            Spacer(Modifier.width(12.dp))
                            Column {
                                Text(ing.name, fontWeight = FontWeight.SemiBold)
                                Text(ing.qty, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileTab(pv: PaddingValues) {
    val multiplier = LocalAccessibilitySettings.current.fontSize.multiplier
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(pv)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size((100 * multiplier).dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size((50 * multiplier).dp), tint = MaterialTheme.colorScheme.onPrimaryContainer)
        }
        Spacer(Modifier.height(12.dp))
        Text("Dueña de casa", fontSize = (22 * multiplier).sp, fontWeight = FontWeight.Bold)
        Text("perfil@ejemplo.com", color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(24.dp))
        Card(onClick = {}, shape = RoundedCornerShape(12.dp)) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Settings, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("Preferencias de recetas")
            }
        }
    }
}

@Preview(name = "Home", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
fun HomeScreen_Preview() {
    val fakeVm = AccessibilityViewModel()
    MaterialTheme {
        HomeScreen(onLogout = {}, accessibilityViewModel = fakeVm)
    }
}
