package com.example.appsemana1.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appsemana1.ui.theme.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

/* ======================= MODELOS ======================= */

enum class UserRole { ADMIN, GUEST }

data class Service(
    val id: String,
    val name: String,
    val duration: Int, // minutos
    val price: Int,
    val isActive: Boolean = true,
    val promotion: Promotion? = null
)

data class Promotion(
    val id: String,
    val discount: Int,             // porcentaje
    val description: String,
    val validUntil: LocalDate
)

data class Appointment(
    val id: String,
    val serviceId: String,
    val date: LocalDate,
    val time: LocalTime,
    val clientName: String = "",
    val status: AppointmentStatus = AppointmentStatus.AVAILABLE
)

enum class AppointmentStatus { AVAILABLE, RESERVED, CONFIRMED, COMPLETED }

data class DailyStats(
    val date: LocalDate,
    val totalAppointments: Int,
    val completedAppointments: Int,
    val revenue: Int
)

/* ======================= UTILIDADES (EXCEPCIONES / INLINE / HOF / EXTENSIONES) ======================= */

class InvalidPromotionException(message: String) : Exception(message)

//Función inline que envuelve un bloque con try/catch y retorna un valor por defecto
inline fun <T> safeOrDefault(
    default: T,
    crossinline onError: (Throwable) -> Unit = {}
): (block: () -> T) -> T = { block ->
    try {
        block()
    } catch (t: Throwable) {
        onError(t)
        default
    }
}

//HOF para aplicar transformación condicional
inline fun <T> List<T>.applyIf(
    condition: Boolean,
    transform: (List<T>) -> List<T>
): List<T> = if (condition) transform(this) else this

//Propiedad de extensión: precio final con promoción (lanza excepción si el descuento es inválido)
val Service.finalPrice: Int
    get() {
        val d = promotion?.discount ?: return price
        if (d !in 0..100) {
            throw InvalidPromotionException("Descuento inválido ($d%) para el servicio: $name")
        }
        return (price * (100 - d)) / 100
    }

//Propiedad de extensión para Appointment, que indica si ya pasó la hora de la cita
val Appointment.isPastNow: Boolean
    get() = date == LocalDate.now() && time.isBefore(LocalTime.now())

//Extensión de lista para ordenar por hora y tomar un límite
fun List<Appointment>.sortedTakeByTime(limit: Int): List<Appointment> =
    this.sortedBy { it.time }.take(limit)

//Lambdas con etiqueta para contar promos válidas sin crashear si falta promoción
fun countValidPromos(services: List<Service>): Int {
    var count = 0
    services.forEach svc@ { s ->
        val p = s.promotion ?: return@svc
        if (p.discount in 1..100) count++
    }
    return count
}

/* ======================= HOME ======================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onOpenSettings: () -> Unit,
    accessibilityViewModel: AccessibilityViewModel
) {
    // Accesibilidad
    val settings = LocalAccessibilitySettings.current
    val multiplier = settings.fontSize.multiplier

    // Manager y diálogo de accesibilidad
    val accManager = rememberAccessibilityManager()
    var showAccDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Estado de la app
    var currentUserRole by remember { mutableStateOf(UserRole.ADMIN) }
    var selectedTab by remember { mutableStateOf(0) }

    // Mock de datos
    val services by remember {
        mutableStateOf(
            listOf(
                Service(
                    "s1", "Corte de Cabello", 30, 15000,
                    promotion = Promotion("p1", 20, "20% OFF este mes", LocalDate.now().plusDays(15))
                ),
                Service("s2", "Manicure", 45, 8000),
                Service("s3", "Pedicure", 60, 12000),
                Service(
                    "s4", "Masaje Relajante", 90, 25000,
                    promotion = Promotion("p2", 15, "15% OFF nuevos clientes", LocalDate.now().plusDays(30))
                ),
                Service("s5", "Tratamiento Facial", 75, 20000)
            )
        )
    }

    val todayAppointments by remember {
        mutableStateOf(
            listOf(
                Appointment("a1", "s1", LocalDate.now(), LocalTime.of(9, 0), "María García", AppointmentStatus.CONFIRMED),
                Appointment("a2", "s2", LocalDate.now(), LocalTime.of(10, 30), "Ana López", AppointmentStatus.RESERVED),
                Appointment("a3", "s1", LocalDate.now(), LocalTime.of(14, 0), status = AppointmentStatus.AVAILABLE),
                Appointment("a4", "s3", LocalDate.now(), LocalTime.of(15, 30), status = AppointmentStatus.AVAILABLE),
                Appointment("a5", "s4", LocalDate.now(), LocalTime.of(17, 0), "Carlos Ruiz", AppointmentStatus.COMPLETED)
            )
        )
    }

    val weekStats by remember {
        mutableStateOf(
            listOf(
                DailyStats(LocalDate.now().minusDays(6), 8, 6, 120_000),
                DailyStats(LocalDate.now().minusDays(5), 12, 10, 180_000),
                DailyStats(LocalDate.now().minusDays(4), 6, 5, 95_000),
                DailyStats(LocalDate.now().minusDays(3), 10, 8, 140_000),
                DailyStats(LocalDate.now().minusDays(2), 14, 12, 220_000),
                DailyStats(LocalDate.now().minusDays(1), 9, 8, 160_000),
                DailyStats(LocalDate.now(), 6, 3, 85_000)
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("AgendaPro", fontWeight = FontWeight.Bold, fontSize = (22 * multiplier).sp)
                        Text(
                            if (currentUserRole == UserRole.ADMIN) "Panel Administrador" else "Portal Cliente",
                            fontSize = (12 * multiplier).sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Badge de rol (toggle para probar)
                    AssistChip(
                        onClick = {
                            currentUserRole = if (currentUserRole == UserRole.ADMIN) UserRole.GUEST else UserRole.ADMIN
                        },
                        label = { Text(if (currentUserRole == UserRole.ADMIN) "ADMIN" else "INVITADO") },
                        leadingIcon = {
                            Icon(
                                if (currentUserRole == UserRole.ADMIN) Icons.Default.ManageAccounts else Icons.Default.Person,
                                contentDescription = null
                            )
                        }
                    )
                    Spacer(Modifier.width(8.dp))

                    // Botón de Accesibilidad
                    accManager.AccessibilityIconButton(onAccessibilityClick = { showAccDialog = true })

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
                NavigationBarItem(
                    selected = selectedTab == 0, onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.Dashboard, null) }, label = { Text("Inicio", fontSize = (12 * multiplier).sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == 1, onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.CalendarToday, null) }, label = { Text("Agenda", fontSize = (12 * multiplier).sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == 2, onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.MiscellaneousServices, null) }, label = { Text("Servicios", fontSize = (12 * multiplier).sp) }
                )
                NavigationBarItem(
                    selected = selectedTab == 3, onClick = { selectedTab = 3 },
                    icon = {
                        Icon(if (currentUserRole == UserRole.ADMIN) Icons.Default.AdminPanelSettings else Icons.Default.Person, null)
                    },
                    label = { Text(if (currentUserRole == UserRole.ADMIN) "Panel" else "Perfil", fontSize = (12 * multiplier).sp) }
                )
            }
        },
        floatingActionButton = {
            if (currentUserRole == UserRole.ADMIN && selectedTab == 1) {
                ExtendedFloatingActionButton(
                    onClick = { /* TODO: abrir diálogo crear cita */ },
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text("Nueva Cita", fontSize = (14 * multiplier).sp) }
                )
            }
        }
    ) { pv ->
        when (selectedTab) {
            0 -> DashboardTab(pv, currentUserRole, todayAppointments, services, weekStats, multiplier)
            1 -> AgendaTab(pv, currentUserRole, todayAppointments, services, multiplier)
            2 -> ServicesTab(pv, currentUserRole, services, multiplier)
            3 -> if (currentUserRole == UserRole.ADMIN)
                AdminPanelTab(pv, services, todayAppointments, multiplier)
            else
                ProfileTab(pv, multiplier, onOpenSettings)
        }
    }

    // Diálogo de accesibilidad
    if (showAccDialog) {
        accManager.AccessibilityDialog(
            currentSettings = settings,
            onSettingsChanged = { new -> accessibilityViewModel.updateSettings(context, new) },
            onDismiss = { showAccDialog = false }
        )
    }
}

/* ======================= TABS ======================= */

@Composable
private fun DashboardTab(
    paddingValues: PaddingValues,
    userRole: UserRole,
    appointments: List<Appointment>,
    services: List<Service>,
    stats: List<DailyStats>,
    multiplier: Float
) {
    val today = stats.lastOrNull()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Resumen del día", fontSize = (24 * multiplier).sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        if (today != null) {
            item {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(
                        listOf(
                            Triple("Citas Hoy", "${today.totalAppointments}", Icons.Default.Today),
                            Triple("Completadas", "${today.completedAppointments}", Icons.Default.CheckCircle),
                            Triple("Ingresos", formatCLP(today.revenue), Icons.Default.AttachMoney),
                            Triple("Servicios", "${services.count { it.isActive }}", Icons.Default.MiscellaneousServices),
                            Triple("Promos válidas", "${countValidPromos(services)}", Icons.Default.LocalOffer) // NUEVO
                        )
                    ) { (title, value, icon) ->
                        StatsCard(title, value, icon, multiplier)
                    }
                }
            }
        }

        item {
            Text("Próximas citas", fontSize = (20 * multiplier).sp, fontWeight = FontWeight.SemiBold)
        }

        items(appointments.sortedTakeByTime(5)) { appointment ->
            AppointmentCard(appointment, services, userRole, multiplier)
        }

        val activePromos = services.filter { it.isActive && it.promotion != null }
        if (activePromos.isNotEmpty()) {
            item { Text("Promociones activas", fontSize = (20 * multiplier).sp, fontWeight = FontWeight.SemiBold) }
            items(activePromos) { svc -> PromotionCard(svc, multiplier) }
        }
    }
}

@Composable
private fun AgendaTab(
    paddingValues: PaddingValues,
    userRole: UserRole,
    appointments: List<Appointment>,
    services: List<Service>,
    multiplier: Float
) {
    val df = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp)
    ) {
        Text("Calendario de citas", fontSize = (24 * multiplier).sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(16.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Hoy - ${LocalDate.now().format(df)}", fontSize = (16 * multiplier).sp, fontWeight = FontWeight.Medium)
                Icon(Icons.Default.CalendarMonth, contentDescription = "Cambiar fecha")
            }
        }

        Spacer(Modifier.height(16.dp))

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(appointments.sortedBy { it.time }) { appt ->
                AppointmentCard(appt, services, userRole, multiplier)
            }
        }
    }
}

@Composable
private fun ServicesTab(
    paddingValues: PaddingValues,
    userRole: UserRole,
    services: List<Service>,
    multiplier: Float
) {
    val listToShow = services.applyIf(userRole == UserRole.GUEST) { list ->
        list.filter { it.isActive }   // para invitados, solo activos
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Nuestros servicios", fontSize = (24 * multiplier).sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                if (userRole == UserRole.ADMIN) {
                    IconButton(onClick = { /* TODO: agregar servicio */ }) { Icon(Icons.Default.Add, contentDescription = "Agregar servicio") }
                }
            }
        }

        items(listToShow) { svc ->
            ServiceCard(svc, userRole, multiplier)
        }
    }
}

@Composable
private fun AdminPanelTab(
    paddingValues: PaddingValues,
    services: List<Service>,
    appointments: List<Appointment>,
    multiplier: Float
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Text("Panel de Administración", fontSize = (24 * multiplier).sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        }

        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                items(
                    listOf(
                        Triple("Nuevo Servicio", "Crear servicio", Icons.Default.Add),
                        Triple("Promociones", "Gestionar ofertas", Icons.Default.LocalOffer),
                        Triple("Horarios", "Configurar agenda", Icons.Default.Schedule),
                        Triple("Reportes", "Ver estadísticas", Icons.Default.Analytics)
                    )
                ) { (title, subtitle, icon) ->
                    AdminActionCard(title, subtitle, icon, multiplier) {
                        /* TODO: implementar acciones */
                    }
                }
            }
        }

        item { Text("Gestión rápida", fontSize = (20 * multiplier).sp, fontWeight = FontWeight.SemiBold) }

        items(services) { svc ->
            AdminServiceCard(svc, multiplier)
        }
    }
}

@Composable
private fun ProfileTab(
    paddingValues: PaddingValues,
    multiplier: Float,
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
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
            Icon(
                Icons.Default.Person,
                contentDescription = null,
                modifier = Modifier.size((50 * multiplier).dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(Modifier.height(16.dp))

        Text("Cliente Invitado", fontSize = (22 * multiplier).sp, fontWeight = FontWeight.Bold)
        Text("invitado@agendapro.com", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = (14 * multiplier).sp)

        Spacer(Modifier.height(24.dp))

        ProfileOptionItem(
            icon = Icons.Default.BookOnline,
            title = "Mis Reservas",
            subtitle = "Ver citas programadas",
            onClick = { /* TODO */ },
            multiplier = multiplier
        )

        ProfileOptionItem(
            icon = Icons.Default.Settings,
            title = "Preferencias",
            subtitle = "Configurar notificaciones",
            onClick = { /* TODO */ },
            multiplier = multiplier
        )

        ProfileOptionItem(
            icon = Icons.Default.Help,
            title = "Ayuda",
            subtitle = "Soporte y FAQ",
            onClick = { /* TODO */ },
            multiplier = multiplier
        )

        ProfileOptionItem(
            icon = Icons.Default.Code,
            title = "Componentes UI Demo",
            subtitle = "Inputs, botones, tablas, grillas, etc.",
            onClick = onOpenSettings,
            multiplier = multiplier
        )
    }
}

/* ======================= COMPONENTES ======================= */

@Composable
private fun ProfileOptionItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    multiplier: Float
) {
    Card(onClick = onClick, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = null)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(title, fontWeight = FontWeight.Medium)
                Text(subtitle, fontSize = (12 * multiplier).sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.Default.ChevronRight, contentDescription = null)
        }
    }
    Spacer(Modifier.height(8.dp))
}

@Composable
private fun StatsCard(title: String, value: String, icon: ImageVector, multiplier: Float) {
    Card(
        modifier = Modifier.width((140 * multiplier).dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size((32 * multiplier).dp), tint = MaterialTheme.colorScheme.onSecondaryContainer)
            Spacer(Modifier.height(8.dp))
            Text(value, fontSize = (20 * multiplier).sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSecondaryContainer)
            Text(title, fontSize = (12 * multiplier).sp, color = MaterialTheme.colorScheme.onSecondaryContainer, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun AppointmentCard(
    appointment: Appointment,
    services: List<Service>,
    userRole: UserRole,
    multiplier: Float
) {
    val service = services.firstOrNull { it.id == appointment.serviceId }
    val timeFmt = remember { DateTimeFormatter.ofPattern("HH:mm") }

    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hora y duración
            Column {
                Text(appointment.time.format(timeFmt), fontSize = (18 * multiplier).sp, fontWeight = FontWeight.Bold)
                Text("${service?.duration ?: 0} min", fontSize = (12 * multiplier).sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }

            Spacer(Modifier.width(16.dp))

            // Detalles
            Column(Modifier.weight(1f)) {
                Text(service?.name ?: "Servicio", fontSize = (16 * multiplier).sp, fontWeight = FontWeight.Medium)
                if (appointment.clientName.isNotBlank()) {
                    Text(appointment.clientName, fontSize = (14 * multiplier).sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                val statusText = when (appointment.status) {
                    AppointmentStatus.AVAILABLE -> "Disponible"
                    AppointmentStatus.RESERVED -> "Reservado"
                    AppointmentStatus.CONFIRMED -> "Confirmado"
                    AppointmentStatus.COMPLETED -> "Completado"
                }
                Text(statusText, fontSize = (12 * multiplier).sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
            }

            // Precio
            if (service != null) {
                Text(
                    formatCLP(service.price),
                    fontSize = (16 * multiplier).sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun ServiceCard(service: Service, userRole: UserRole, multiplier: Float) {
    Card(modifier = Modifier.fillMaxWidth(), onClick = { /* TODO: detalles */ }) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column(Modifier.weight(1f)) {
                    Text(service.name, fontSize = (18 * multiplier).sp, fontWeight = FontWeight.Bold)
                    Text("${service.duration} minutos", fontSize = (14 * multiplier).sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }

                val computeOrZero = safeOrDefault(default = 0) { /* pueds loguea si quiere */ }

                Column(horizontalAlignment = Alignment.End) {
                    val hasPromo = service.promotion != null
                    val basePrice = service.price
                    val discountedPrice = computeOrZero {
                        service.finalPrice //puede lanzar InvalidPromotionException si el descuento es inválido
                    }

                    if (hasPromo) {
                        Text(
                            formatCLP(basePrice),
                            fontSize = (14 * multiplier).sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = TextStyle(textDecoration = TextDecoration.LineThrough)
                        )
                        Text(
                            formatCLP(discountedPrice),
                            fontSize = (18 * multiplier).sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    } else {
                        Text(
                            formatCLP(basePrice),
                            fontSize = (18 * multiplier).sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            if (service.promotion != null) {
                Spacer(Modifier.height(8.dp))
                Card(colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
                    Text(
                        "${service.promotion.discount}% OFF - ${service.promotion.description}",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = (12 * multiplier).sp,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            if (userRole == UserRole.GUEST) {
                Spacer(Modifier.height(12.dp))
                Button(onClick = { /* TODO: reservar */ }, modifier = Modifier.fillMaxWidth()) {
                    Text("Reservar Cita", fontSize = (14 * multiplier).sp)
                }
            }
        }
    }
}

@Composable
private fun PromotionCard(service: Service, multiplier: Float) {
    val promotion = service.promotion ?: return
    val df = remember { DateTimeFormatter.ofPattern("dd/MM/yyyy") }

    Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.LocalOffer, contentDescription = null, modifier = Modifier.size((32 * multiplier).dp), tint = MaterialTheme.colorScheme.onErrorContainer)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text("${promotion.discount}% OFF en ${service.name}", fontSize = (16 * multiplier).sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onErrorContainer)
                Text(promotion.description, fontSize = (14 * multiplier).sp, color = MaterialTheme.colorScheme.onErrorContainer)
                Text("Válido hasta ${promotion.validUntil.format(df)}", fontSize = (12 * multiplier).sp, color = MaterialTheme.colorScheme.onErrorContainer)
            }
        }
    }
}

@Composable
private fun AdminActionCard(
    title: String,
    subtitle: String,
    icon: ImageVector,
    multiplier: Float,
    onClick: () -> Unit
) {
    Card(onClick = onClick, modifier = Modifier.width((160 * multiplier).dp)) {
        Column(Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size((40 * multiplier).dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(8.dp))
            Text(title, fontSize = (14 * multiplier).sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
            Text(subtitle, fontSize = (12 * multiplier).sp, color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun AdminServiceCard(service: Service, multiplier: Float) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(service.name, fontWeight = FontWeight.SemiBold)
                Text("${service.duration} min • ${formatCLP(service.price)}", style = MaterialTheme.typography.bodySmall)
                Text(if (service.isActive) "Activo" else "Inactivo", style = MaterialTheme.typography.bodySmall)
            }
            IconButton(onClick = { /* TODO editar */ }) { Icon(Icons.Default.Edit, null) }
            IconButton(onClick = { /* TODO activar/desactivar */ }) {
                Icon(if (service.isActive) Icons.Default.VisibilityOff else Icons.Default.Visibility, null)
            }
            IconButton(onClick = { /* TODO eliminar (validar reservas futuras) */ }) { Icon(Icons.Default.Delete, null) }
        }
    }
}

/* ======================= HELPERS ======================= */

private fun formatCLP(value: Int): String {
    val s = "%,d".format(Locale("es", "CL"), value).replace(',', '.')
    return "$$s"
}

/* ======================= PREVIEWS ======================= */

@Preview(name = "AgendaPro Home - Admin", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun HomeScreen_Admin_Preview() {
    val fakeViewModel = AccessibilityViewModel()
    AppSemana1Theme {
        HomeScreen(
            onLogout = {},
            onOpenSettings = {},
            accessibilityViewModel = fakeViewModel
        )
    }
}

@Preview(name = "AgendaPro Home - Guest", showBackground = true, widthDp = 360, heightDp = 720)
@Composable
private fun HomeScreen_Guest_Preview() {
    val fakeViewModel = AccessibilityViewModel()
    AppSemana1Theme(
        accessibilitySettings = AccessibilitySettings(
            fontSize = FontSize.LARGE,
            colorblindType = ColorblindType.PROTANOPIA,
            isColorblindMode = true
        )
    ) {
        HomeScreen(
            onLogout = {},
            onOpenSettings = {},
            accessibilityViewModel = fakeViewModel
        )
    }
}
