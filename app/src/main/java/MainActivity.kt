package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

private val Coral = Color(0xFFE76F51)
private val DeepNavy = Color(0xFF172B4D)
private val SoftBackground = Color(0xFFFFFBF8)
private val Muted = Color(0xFF68738A)

private data class Person(
    val name: String,
    val age: Int,
    val city: String,
    val initials: String,
    val color: Color,
    val interests: List<String>,
    val description: String
)

private val people = listOf(
    Person("Valentina", 24, "Buenos Aires", "VA", Color(0xFFF4A261), listOf("Café", "Fotografía", "Viajes"), "Colecciono atardeceres y lugares tranquilos para conversar."),
    Person("Mateo", 27, "Córdoba", "MA", Color(0xFF6D9DC5), listOf("Música", "Gaming", "Cocina"), "Siempre tengo una playlist nueva y una receta para compartir."),
    Person("Sofía", 23, "Rosario", "SO", Color(0xFF9B8AFB), listOf("Libros", "Arte", "Yoga"), "Busco gente curiosa para descubrir planes diferentes."),
    Person("Nicolás", 29, "Mendoza", "NI", Color(0xFF76B39D), listOf("Senderismo", "Películas", "Comida"), "Fan de los cerros, las películas malas y el buen humor.")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { MyApplicationTheme { ConnectaApp() } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ConnectaApp() {
    var selectedTab by remember { mutableIntStateOf(0) }
    var currentProfile by remember { mutableIntStateOf(0) }
    var likedProfiles by remember { mutableStateOf(setOf<String>()) }
    var showFilters by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = SoftBackground,
        topBar = {
            TopAppBar(
                title = { Text("conecta", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 25.sp) },
                actions = {
                    IconButton(onClick = { showFilters = !showFilters }) { Text("Filtros", color = DeepNavy, fontSize = 13.sp) }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = SoftBackground)
            )
        },
        bottomBar = {
            BottomNavigation(selectedTab = selectedTab, onSelect = { selectedTab = it })
        }
    ) { padding ->
        Box(Modifier.padding(padding).fillMaxSize()) {
            when (selectedTab) {
                0 -> DiscoverScreen(
                    person = people[currentProfile],
                    liked = likedProfiles.contains(people[currentProfile].name),
                    onPass = { currentProfile = (currentProfile + 1) % people.size },
                    onLike = {
                        likedProfiles = likedProfiles + people[currentProfile].name
                        currentProfile = (currentProfile + 1) % people.size
                    },
                    onOpenProfile = { }
                )
                1 -> MatchesScreen(likedProfiles)
                2 -> MessagesScreen()
                else -> ProfileScreen()
            }
            if (showFilters) FilterPanel(onDismiss = { showFilters = false })
        }
    }
}

@Composable
private fun DiscoverScreen(person: Person, liked: Boolean, onPass: () -> Unit, onLike: () -> Unit, onOpenProfile: () -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(Modifier.height(8.dp))
        Text("Descubre personas cerca de ti", style = MaterialTheme.typography.titleMedium, color = DeepNavy, fontWeight = FontWeight.SemiBold)
        Text("Conecta por intereses, no solo por apariencia.", color = Muted, fontSize = 14.sp)
        Spacer(Modifier.height(18.dp))
        ProfileCard(person, onOpenProfile)
        Spacer(Modifier.height(18.dp))
        if (liked) Text("Te interesa este perfil", color = Coral, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(18.dp), verticalAlignment = Alignment.CenterVertically) {
            OutlinedButton(onClick = onPass, shape = CircleShape, modifier = Modifier.size(68.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)) { Text("No", color = DeepNavy, fontWeight = FontWeight.Bold) }
            Button(onClick = onLike, shape = CircleShape, modifier = Modifier.size(78.dp), contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp), colors = ButtonDefaults.buttonColors(containerColor = Coral)) { Text("Sí", fontWeight = FontWeight.Bold) }
        }
        Spacer(Modifier.height(10.dp))
        Text("Desliza tu decisión con calma", color = Muted, fontSize = 12.sp)
    }
}

@Composable
private fun ProfileCard(person: Person, onClick: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick), shape = RoundedCornerShape(28.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(3.dp)) {
        Column {
            Box(Modifier.fillMaxWidth().height(260.dp).background(person.color), contentAlignment = Alignment.Center) {
                Text(person.initials, color = Color.White, fontSize = 72.sp, fontWeight = FontWeight.Bold)
                Surface(Modifier.align(Alignment.BottomStart).padding(16.dp), shape = RoundedCornerShape(14.dp), color = Color.White.copy(alpha = .92f)) { Text("EN LÍNEA", Modifier.padding(horizontal = 10.dp, vertical = 5.dp), color = Color(0xFF39856D), fontSize = 11.sp, fontWeight = FontWeight.Bold) }
            }
            Column(Modifier.padding(18.dp)) {
                Text("${person.name}, ${person.age}", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                Text("${person.city}  ·  A 3 km de distancia", color = Muted, fontSize = 13.sp)
                Spacer(Modifier.height(10.dp))
                Text(person.description, color = DeepNavy, lineHeight = 20.sp)
                Spacer(Modifier.height(12.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(7.dp)) { person.interests.forEach { InterestTag(it) } }
            }
        }
    }
}

@Composable
private fun InterestTag(text: String) { Surface(shape = RoundedCornerShape(50), color = Color(0xFFFFEEE9)) { Text(text, Modifier.padding(horizontal = 10.dp, vertical = 6.dp), color = Coral, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) } }

@Composable
private fun MatchesScreen(likedProfiles: Set<String>) {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Tus conexiones", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
        Text("Personas con las que podrías iniciar una charla.", color = Muted)
        Spacer(Modifier.height(18.dp))
        if (likedProfiles.isEmpty()) EmptyState("Todavía no tienes conexiones", "Explora perfiles y marca a quienes te gustaría conocer.")
        else LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) { items(people.filter { likedProfiles.contains(it.name) }) { person -> MatchRow(person) } }
    }
}

@Composable
private fun MatchRow(person: Person) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
        Row(Modifier.fillMaxWidth().padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
            Avatar(person, 56.dp)
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) { Text(person.name, color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 17.sp); Text("También le interesan ${person.interests.first()}", color = Muted, fontSize = 13.sp) }
            Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = Coral), shape = RoundedCornerShape(12.dp)) { Text("Saludar") }
        }
    }
}

@Composable
private fun MessagesScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Mensajes", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
        Text("Tus conversaciones empiezan con un hola.", color = Muted)
        Spacer(Modifier.height(18.dp))
        val chats = listOf(Pair(people[1], "¡Hola! Vi que también te gusta cocinar."), Pair(people[2], "¿Qué libro estás leyendo?"))
        LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) { items(chats) { (person, message) -> ChatRow(person, message) } }
    }
}

@Composable
private fun ChatRow(person: Person, message: String) { Row(Modifier.fillMaxWidth().padding(vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) { Avatar(person, 52.dp); Spacer(Modifier.width(12.dp)); Column { Text(person.name, color = DeepNavy, fontWeight = FontWeight.Bold); Text(message, color = Muted, fontSize = 13.sp) } } }

@Composable
private fun ProfileScreen() {
    Column(Modifier.fillMaxSize().padding(20.dp)) {
        Text("Mi perfil", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
        Spacer(Modifier.height(18.dp))
        Row(verticalAlignment = Alignment.CenterVertically) { Avatar(Person("Tú", 25, "", "TU", Color(0xFF345995), emptyList(), ""), 78.dp); Spacer(Modifier.width(16.dp)); Column { Text("Tu nombre", fontSize = 21.sp, fontWeight = FontWeight.Bold, color = DeepNavy); Text("Buenos Aires · 25 años", color = Muted) } }
        Spacer(Modifier.height(24.dp))
        Text("Sobre mí", fontWeight = FontWeight.Bold, color = DeepNavy)
        Spacer(Modifier.height(6.dp))
        Text("Cuéntale a la comunidad qué te entusiasma y qué tipo de conexión estás buscando.", color = Muted, lineHeight = 21.sp)
        Spacer(Modifier.height(20.dp))
        OutlinedTextField(value = "", onValueChange = {}, label = { Text("Escribe una presentación") }, modifier = Modifier.fillMaxWidth(), minLines = 3)
        Spacer(Modifier.height(16.dp))
        Button(onClick = {}, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = Coral), shape = RoundedCornerShape(14.dp)) { Text("Guardar cambios") }
    }
}

@Composable
private fun Avatar(person: Person, size: androidx.compose.ui.unit.Dp) { Box(Modifier.size(size).clip(CircleShape).background(person.color), contentAlignment = Alignment.Center) { Text(person.initials, color = Color.White, fontWeight = FontWeight.Bold, fontSize = (size.value / 3).sp) } }

@Composable
private fun BottomNavigation(selectedTab: Int, onSelect: (Int) -> Unit) {
    Surface(color = Color.White, tonalElevation = 4.dp, modifier = Modifier.navigationBarsPadding()) {
        Row(Modifier.fillMaxWidth().padding(vertical = 10.dp), horizontalArrangement = Arrangement.SpaceAround) {
            listOf("Descubrir", "Conexiones", "Mensajes", "Perfil").forEachIndexed { index, label ->
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onSelect(index) }.padding(horizontal = 8.dp)) {
                    Text(label, color = if (selectedTab == index) Coral else Muted, fontSize = 12.sp, fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal)
                    if (selectedTab == index) Box(Modifier.padding(top = 4.dp).size(5.dp).clip(CircleShape).background(Coral))
                }
            }
        }
    }
}

@Composable
private fun FilterPanel(onDismiss: () -> Unit) {
    Surface(Modifier.fillMaxWidth().padding(12.dp), shape = RoundedCornerShape(20.dp), color = Color.White, shadowElevation = 8.dp) {
        Column(Modifier.padding(18.dp)) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) { Text("Preferencias", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 18.sp); Text("Cerrar", color = Coral, modifier = Modifier.clickable(onClick = onDismiss), fontSize = 13.sp) }
            Spacer(Modifier.height(12.dp)); Text("Me interesa conocer gente que disfrute", color = Muted, fontSize = 13.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 10.dp)) { listOf("Música", "Viajes", "Deportes").forEach { FilterChip(selected = false, onClick = {}, label = { Text(it) }) } }
        }
    }
}

@Composable
private fun EmptyState(title: String, message: String) { Column(Modifier.fillMaxWidth().padding(top = 80.dp), horizontalAlignment = Alignment.CenterHorizontally) { Text("Aún no", color = Coral, fontWeight = FontWeight.Bold); Spacer(Modifier.height(8.dp)); Text(title, color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 20.sp); Text(message, color = Muted, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 8.dp)) } }

@Preview(showBackground = true)
@Composable
private fun ConnectaPreview() { MyApplicationTheme { ConnectaApp() } }
