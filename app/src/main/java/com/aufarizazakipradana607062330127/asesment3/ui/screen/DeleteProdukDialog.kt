package com.aufarizazakipradana607062330127.asesment3.ui.screen

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.aufarizazakipradana607062330127.asesment3.R
import com.aufarizazakipradana607062330127.asesment3.model.KelolaProduk

@Composable
fun DeleteProdukDialog(
    produk: KelolaProduk,
    onDismissRequest: () -> Unit,
    onConfirmDelete: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            Button(onClick = onConfirmDelete) {
                Text(text = stringResource(id = R.string.hapus))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(text = stringResource(id = R.string.batal))
            }
        },
        title = {
            Text(text = stringResource(id = R.string.konfirmasi_hapus))
        },
        text = {
            Text(text = "Apakah kamu yakin ingin menghapus produk \"${produk.brandName}\"?")
        },
        shape = RoundedCornerShape(16.dp)
    )
}
