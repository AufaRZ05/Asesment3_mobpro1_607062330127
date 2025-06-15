package com.aufarizazakipradana607062330127.asesment3.ui.screen

import android.content.res.Configuration
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.aufarizazakipradana607062330127.asesment3.R
import com.aufarizazakipradana607062330127.asesment3.model.KelolaProduk
import com.aufarizazakipradana607062330127.asesment3.ui.theme.Asesment3Theme

@Composable
fun KelolaProdukDialog(
    bitmap: Bitmap?,
    produk: KelolaProduk? = null,
    onDismissRequest: () -> Unit,
    onConfirm: (
        brandName: String,
        price: String,
        stock: String,
        category: String
    ) -> Unit,
    onImageSelected: () -> Unit
) {
    var brandName by remember { mutableStateOf(produk?.brandName ?: "") }
    var price by remember { mutableStateOf(produk?.price?.toString() ?: "") }
    var stock by remember { mutableStateOf(produk?.stock?.toString() ?: "") }
    var category by remember { mutableStateOf(produk?.category ?: "") }

    val isEditMode = produk != null

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(
                onClick = {
                    onConfirm(brandName, price, stock, category)
                },
                enabled = brandName.isNotBlank() && price.isNotBlank() && stock.isNotBlank() && category.isNotBlank()
            ) {
                Text(text = stringResource(id = if (isEditMode) R.string.update else R.string.simpan))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.batal))
            }
        },
        title = {
            Text(text = stringResource(id = if (isEditMode) R.string.edit_produk else R.string.tambah_produk_baru))
        },
        text = {
            Column {
                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                } else if (isEditMode && produk?.imageUrl != null) {
                    AsyncImage(
                        model = produk.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit,
                        placeholder = painterResource(id = R.drawable.loading_img),
                        error = painterResource(id = R.drawable.broken_img)
                    )
                } else {
                    Image(
                        painter = painterResource(id = R.drawable.broken_img),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .padding(8.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                if (isEditMode) {
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedButton(
                        onClick = onImageSelected,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.ganti_foto))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = brandName,
                    onValueChange = { brandName = it },
                    label = { Text(text = stringResource(id = R.string.nama_merek)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text(text = stringResource(id = R.string.harga)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = stock,
                    onValueChange = { stock = it },
                    label = { Text(text = stringResource(id = R.string.stok)) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = category,
                    onValueChange = { category = it },
                    label = { Text(text = stringResource(id = R.string.kategori
                    )) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        shape = RoundedCornerShape(16.dp)
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun AddDialogPreview() {
    Asesment3Theme  {
        KelolaProdukDialog(
            bitmap = null,
            onDismissRequest = {},
            onConfirm = { _, _, _, _ -> },
            onImageSelected = {}
        )
    }
}