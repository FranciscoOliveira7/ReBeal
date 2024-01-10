# Desenvolvimento de Jogos para Plataformas Móveis | ReBeal

---

## Trabalho realizado por:

- **Francisco Oliveira** - 25979

- **Gonçalo Silva** - 25970

- **Rui Costa** - 25959

## Indice

- Objetivos

- "Actual Features" (Objetivos compridos)

- Implementação de Firebase em Android

- Classes / Activities 

## Objetivos

Este projeto tem como objetivo criar uma rede social à base de posts.

Os posts contêm uma imagem e um breve comentário, tal como um botão de like e de comentar.

##### Layout da Aplicação

| 🏠 Feed                                                                           | 📷 Adicionar Post           | 👤 Perfil                                                                          |
|:---------------------------------------------------------------------------------:|:---------------------------:|:----------------------------------------------------------------------------------:|
| Página principal onde se encontra todos os posts apresentados em ordem aleatória. | Página de criação de posts. | Perfil do utilizador onde inclui os seus posts como também nome de foto de perfil. |
|                                                                                   | (login necessário)          | (login necessário)                                                                 |

## Implementação de Firebase em Android

---

### Importação do Firebase para o projeto

#### Gradle

`build.gradle.kts` (Module :app)

```kts
dependencies {
    implementation("com.google.firebase:firebase-database-ktx:20.3.0")
    implementation("com.google.firebase:firebase-firestore-ktx:24.10.0")

    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-database")
}
```

#### Packages

Package principal

`import com.google.firebase.Firebase`

Autenticação

`import com.google.firebase.auth.FirebaseAuth`

`import com.google.firebase.auth.auth`

Firestore (Base de dados flexivél)
`import com.google.firebase.firestore.FirebaseFirestore`

### Utilização de Firebase no projeto

Verificar autenticação do utilizador

```kt
// Ir para a activity de login caso ainda não esteja autenticado
if (auth.currentUser == null) {
    lifecycleScope.launch(Dispatchers.IO) {
        Thread.sleep(1000L)
        withContext(Dispatchers.Main) {
            val intent = Intent(requireContext(), LoginActivity::class.java)
            startActivity(intent)
        }
    }
} else { // Coletar o nome de utilizador caso contrário
    val uid = auth.currentUser?.uid
    val userDocRef = firestore.collection("users").document(uid!!)
    userDocRef.get().addOnSuccessListener { documentSnapshot ->
        val username = documentSnapshot.getString("username")
        binding.Utilizador.text = username
    }
}
```
