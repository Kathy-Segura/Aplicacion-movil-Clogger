package com.appsandroid.clogger.utils

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.graphics.Canvas as AndroidCanvas
import android.graphics.Paint as AndroidPaint
import java.io.File
import java.io.FileWriter

fun createPdf(context: Context, filename: String, title: String, bodyLines: List<String>): File {
    val pdfDocument = PdfDocument()
    val pageInfo = PdfDocument.PageInfo.Builder(595, 842, 1).create()
    val page = pdfDocument.startPage(pageInfo)

    val canvas: AndroidCanvas = page.canvas   // Canvas de android.graphics
    val paint = AndroidPaint().apply {
        textSize = 14f   // ahora sí lo reconoce
        color = android.graphics.Color.BLACK
    }

    var y = 40f
    canvas.drawText(title, 40f, y, paint)
    y += 30f

    bodyLines.forEach { line ->
        canvas.drawText(line, 40f, y, paint)
        y += 20f
    }

    pdfDocument.finishPage(page)

    val file = File(context.cacheDir, "$filename.pdf")
    pdfDocument.writeTo(file.outputStream())
    pdfDocument.close()
    return file
}

// exportCsv (sin cambios funcionales)
fun exportCsv(context: Context, filename: String, headers: List<String>, rows: List<List<String>>): File {
    val file = File(context.cacheDir, "$filename.csv")
    FileWriter(file).use { writer ->
        writer.append(headers.joinToString(",")).append("\n")
        rows.forEach { row ->
            writer.append(row.joinToString(",")).append("\n")
        }
    }
    return file
}
