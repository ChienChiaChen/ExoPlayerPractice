package com.chiachen.exoplayerpractice.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LoadingItem() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorItem(
    message: String,
    onRetryClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = message,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.error
        )
        TextButton(onClick = onRetryClick) {
            Text("Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoadingItemPreview() {
    MaterialTheme {
        LoadingItem()
    }
}

@Preview(showBackground = true)
@Composable
private fun ErrorItemPreview() {
    MaterialTheme {
        ErrorItem(
            message = "Failed to load videos. Please check your internet connection.",
            onRetryClick = {}
        )
    }
}

@Preview(showBackground = true, widthDp = 300)
@Composable
private fun ErrorItemPreviewNarrow() {
    MaterialTheme {
        ErrorItem(
            message = "Failed to load videos. Please check your internet connection.",
            onRetryClick = {}
        )
    }
} 