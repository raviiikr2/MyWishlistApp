package eu.com.mywishlistapp

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


fun ShareWishAsPdf(context: Context, wish: Wish) {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(600, 800, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    val canvas = page.canvas
    val paint = Paint().apply {
        textSize = 18f
        isFakeBoldText = true
    }

    // Title
    canvas.drawText("Wish Title: ${wish.title}", 40f, 60f, paint)

    // Description
    paint.textSize = 16f
    paint.isFakeBoldText = false
    val lines = wish.description.split("\n")
    var y = 120f
    for (line in lines) {
        canvas.drawText(line, 40f, y, paint)
        y += paint.textSize + 10f
    }

    pdfDocument.finishPage(page)

    // Save file
    val file = File(context.cacheDir, "${wish.title}.pdf")
    pdfDocument.writeTo(FileOutputStream(file))
    pdfDocument.close()

    // Share intent
    val uri = FileProvider.getUriForFile(
        context,
        "${context.packageName}.provider",
        file
    )

    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share Wish as PDF"))
}
