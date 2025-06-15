package com.aufarizazakipradana607062330127.asesment3.ui.screen

import android.content.ContentResolver
import android.content.Context
import android.content.res.Configuration
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.aufarizazakipradana607062330127.asesment3.BuildConfig
import com.aufarizazakipradana607062330127.asesment3.R
import com.aufarizazakipradana607062330127.asesment3.model.KelolaProduk
import com.aufarizazakipradana607062330127.asesment3.model.User
import com.aufarizazakipradana607062330127.asesment3.network.ApiStatus
import com.aufarizazakipradana607062330127.asesment3.network.UserDataStore
import com.aufarizazakipradana607062330127.asesment3.ui.theme.Asesment3Theme
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(){
    val context = LocalContext.current
    val dataStore = UserDataStore(context)
    val user by dataStore.userFlow.collectAsState(User())

    val viewModel: MainViewModel = viewModel()
    val errorMessage by viewModel.errorMessage

    var showDialog by remember { mutableStateOf(false) }
    var showKelolaProdukDialog by remember { mutableStateOf(false) }

    var showDeleteDialog by remember { mutableStateOf(false) }
    var produkToDelete by remember { mutableStateOf<KelolaProduk?>(null) }

    var showEditProdukDialog by remember { mutableStateOf(false) }
    var produkToEdit by remember { mutableStateOf<KelolaProduk?>(null) }

    var bitmap: Bitmap? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(CropImageContract()) {
        bitmap = getCroppedImage(context.contentResolver, it)
        if (bitmap != null) {
            if (produkToEdit == null) {
                showKelolaProdukDialog = true
            } else {
                showEditProdukDialog = true
            }
        }
    }

    LaunchedEffect(user.email) {
        if (user.email.isNotEmpty()) {
            viewModel.retrieveData(user.email)
        } else {
            viewModel.retrieveData("all")
        }
    }

    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    IconButton(onClick = {
                        if (user.email.isEmpty()) {
                            CoroutineScope(Dispatchers.IO).launch { signIn(context, dataStore) }
                        }
                        else {
                            showDialog = true
                        }
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_account_circle_24),
                            contentDescription = stringResource(R.string.profil),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                val options = CropImageContractOptions(
                    null, CropImageOptions(
                        imageSourceIncludeGallery = false,
                        imageSourceIncludeCamera = true,
                        fixAspectRatio = true
                    )
                )
                launcher.launch(options)
            }) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(id = R.string.tambah_produk)
                )
            }
        }
    ) { innerPadding ->
        ScreenContent(
            viewModel = viewModel,
            modifier = Modifier.padding(innerPadding),
            userId = user.email,
            onDeleteClick = { produk ->
                produkToDelete = produk
                showDeleteDialog = true
            },
            onEditClick = { produk ->
                produkToEdit = produk
                showEditProdukDialog = true
                bitmap = null
            }
        )

        if (showDialog) {
            ProfilDialog(
                user = user,
                onDismissRequest = { showDialog = false }) {
                CoroutineScope(Dispatchers.IO).launch { signOut(context, dataStore) }
                showDialog = false
            }
        }

        if (showKelolaProdukDialog && produkToEdit == null) {
            KelolaProdukDialog(
                bitmap = bitmap,
                onDismissRequest = { showKelolaProdukDialog = false; bitmap = null },
                onConfirm = { brandName, price, stock, category ->
                    Toast.makeText(context, "Menyimpan produk...", Toast.LENGTH_SHORT).show()
                    val priceInt = price.toIntOrNull() ?: 0
                    val stockInt = stock.toIntOrNull() ?: 0
                    viewModel.saveData(user.email, brandName, priceInt, stockInt, category, bitmap!!)
                    showKelolaProdukDialog = false
                    bitmap = null
                },
                onImageSelected = {  }
            )
        }

        if (showDeleteDialog && produkToDelete != null) {
            DeleteProdukDialog(
                produk = produkToDelete!!,
                onDismissRequest = { showDeleteDialog = false },
                onConfirmDelete = {
                    viewModel.deleteData(produkToDelete!!.id, produkToDelete!!.userId)
                    showDeleteDialog = false
                }
            )
        }

        if (showDeleteDialog && produkToDelete != null) {
            DeleteProdukDialog(
                produk = produkToDelete!!,
                onDismissRequest = {
                    showDeleteDialog = false
                    produkToDelete = null
                },
                onConfirmDelete = {
                    viewModel.deleteData(produkToDelete!!.id, user.email)
                    showDeleteDialog = false
                    produkToDelete = null
                }
            )
        }

        if (showEditProdukDialog && produkToEdit != null) {
            KelolaProdukDialog(
                bitmap = bitmap,
                produk = produkToEdit,
                onDismissRequest = { showEditProdukDialog = false; produkToEdit = null; bitmap = null },
                onConfirm = { brandName, price, stock, category ->
                    Toast.makeText(context, "Mengupdate produk...", Toast.LENGTH_SHORT).show()
                    val priceInt = price.toIntOrNull() ?: 0
                    val stockInt = stock.toIntOrNull() ?: 0
                    viewModel.updateData(
                        id = produkToEdit!!.id,
                        userId = user.email,
                        brandName = brandName,
                        price = priceInt,
                        stock = stockInt,
                        category = category,
                        bitmap = bitmap
                    )
                    showEditProdukDialog = false
                    produkToEdit = null
                    bitmap = null
                },
                onImageSelected = {
                    val options = CropImageContractOptions(
                        null, CropImageOptions(
                            imageSourceIncludeGallery = true,
                            imageSourceIncludeCamera = true,
                            fixAspectRatio = true
                        )
                    )
                    launcher.launch(options)
                }
            )
        }

        if (errorMessage != null) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
            viewModel.clearMessage()
        }
    }
}

@Composable
fun ScreenContent(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    userId: String,
    onDeleteClick: (KelolaProduk) -> Unit,
    onEditClick: (KelolaProduk) -> Unit
) {
    val data by viewModel.data
    val status by viewModel.status.collectAsState()

    when (status) {
        ApiStatus.LOADING -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        ApiStatus.SUCCESS -> {
            if (data.isNotEmpty()) {
                LazyVerticalGrid(
                    modifier = modifier.fillMaxSize().padding(4.dp),
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(bottom = 80.dp)
                ) {
                    items(data) { kelolaProduk ->
                        ListItem(
                            kelolaproduk = kelolaProduk,
                            onEditClick = onEditClick,
                            onDeleteClick = onDeleteClick
                        )
                    }
                }
            } else {
                Box(
                    modifier = modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Belum ada produk untuk ditampilkan.")
                }
            }
        }

        ApiStatus.FAILED -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = stringResource(id = R.string.error))
                Button(
                    onClick = { viewModel.retrieveData(userId) },
                    modifier = Modifier.padding(top = 16.dp),
                    contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp)
                ) {
                    Text(text = stringResource(id = R.string.try_again))
                }
            }
        }
    }
}

@Composable
fun ListItem(
    kelolaproduk: KelolaProduk,
    onDeleteClick: (KelolaProduk) -> Unit,
    onEditClick: (KelolaProduk) -> Unit
) {
    Box(
        modifier = Modifier.padding(4.dp).border(1.dp, Color.Gray),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = kelolaproduk.imageUrl,
            contentDescription = stringResource(R.string.gambar, kelolaproduk.brandName),
            contentScale = ContentScale.Crop,
            placeholder = painterResource(id = R.drawable.loading_img),
            error = painterResource(id = R.drawable.broken_img),
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color(0x99000000))
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = kelolaproduk.brandName,
                style = MaterialTheme.typography.titleMedium,
                color = Color.White
            )
            Text(
                text = "Rp ${kelolaproduk.price}",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = "${kelolaproduk.stock} Item",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            Text(
                text = kelolaproduk.category,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White
            )
            IconButton(
                onClick = {
                    onEditClick(kelolaproduk)
                },
                modifier = Modifier.padding(end = 4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_edit_24),
                    contentDescription = "Edit",
                    tint = Color.White
                )
            }
            IconButton(
                onClick = {
                    onDeleteClick(kelolaproduk)
                }
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_delete_24),
                    contentDescription = "Hapus",
                    tint = Color.White
                )
            }

        }
    }
}

private suspend fun signIn(context: Context, dataStore: UserDataStore) {
    val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
        .setFilterByAuthorizedAccounts(false)
        .setServerClientId(BuildConfig.API_KEY)
        .build()

    val request: GetCredentialRequest = GetCredentialRequest.Builder()
        .addCredentialOption(googleIdOption)
        .build()

    try {
        val credentialManager = CredentialManager.create(context)
        val result = credentialManager.getCredential(context, request)
        handleSignIn(result, dataStore)
    } catch (e: GetCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private suspend fun handleSignIn(
    result: GetCredentialResponse,
    dataStore: UserDataStore
) {
    val credential = result.credential
    if (credential is CustomCredential &&
        credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
        try {
            val googleId = GoogleIdTokenCredential.createFrom(credential.data)
            val nama = googleId.displayName ?: ""
            val email = googleId.id
            val photoUrl = googleId.profilePictureUri.toString()
            dataStore.saveData(User(nama, email, photoUrl))
        } catch (e: GoogleIdTokenParsingException) {
            Log.e("SIGN-IN", "Error: ${e.message}")
        }
    }
    else {
        Log.e("SIGN-IN", "Error: unrecognized custom credential type.")
    }
}

private suspend fun signOut(context: Context, dataStore: UserDataStore) {
    try {
        val credentialManager = CredentialManager.create(context)
        credentialManager.clearCredentialState(
            ClearCredentialStateRequest()
        )
        dataStore.saveData(User())
    } catch (e: ClearCredentialException) {
        Log.e("SIGN-IN", "Error: ${e.errorMessage}")
    }
}

private fun getCroppedImage(
    resolver: ContentResolver,
    result: CropImageView.CropResult
): Bitmap? {
    if (!result.isSuccessful) {
        Log.e("IMAGE", "Error: ${result.error}")
        return null
    }
    val uri = result.uriContent ?: return null

    return  if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P) {
        MediaStore.Images.Media.getBitmap(resolver, uri)
    } else {
        val source = ImageDecoder.createSource(resolver, uri)
        ImageDecoder.decodeBitmap(source)
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun MainScreenPreview() {
    Asesment3Theme {
        MainScreen()
    }
}