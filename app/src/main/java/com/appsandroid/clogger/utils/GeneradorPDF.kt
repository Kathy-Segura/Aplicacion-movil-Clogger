package com.appsandroid.clogger.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import com.appsandroid.clogger.data.model.BoletinAgricola
import java.io.File
import java.io.FileOutputStream

object PdfGenerator {

    fun crearBoletinPdf(context: Context, boletin: BoletinAgricola, dias: List<GenerarBoletinUseCase.DiaResumen>): File {
        val pdf = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val margin = 36

        var yPos = margin

        fun newPage(pageNumber: Int): PdfDocument.Page {
            val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create()
            val page = pdf.startPage(pageInfo)
            yPos = margin
            return page
        }

        val titlePaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = 18f
            isAntiAlias = true
            color = android.graphics.Color.BLACK
        }

        val headerPaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.BOLD)
            textSize = 12f
            isAntiAlias = true
        }

        val normalPaint = Paint().apply {
            typeface = Typeface.create(Typeface.SANS_SERIF, Typeface.NORMAL)
            textSize = 11f
            isAntiAlias = true
        }

        var page = newPage(1)
        var canvas = page.canvas

        fun writeLine(text: String, paint: Paint, addGap: Boolean = true) {
            val lines = text.split("\n")
            for (line in lines) {
                if (yPos > pageHeight - margin - 40) {
                    pdf.finishPage(page)
                    page = newPage(pdf.pages.size + 1)
                    canvas = page.canvas
                }
                canvas.drawText(line, margin.toFloat(), yPos.toFloat(), paint)
                yPos += (paint.textSize + 8).toInt()
            }
            if (addGap) yPos += 6
        }

        // Header
        writeLine("BOLETÍN AGROCLIMÁTICO", titlePaint)
        writeLine("Zona: ${boletin.zona}", headerPaint)
        writeLine("Fecha de generación: ${boletin.fechaGeneracion}", headerPaint)
        yPos += 6

        // Resumen
        writeLine("--- RESUMEN CLIMÁTICO ---", headerPaint)
        writeLine(boletin.resumenClimatico, normalPaint)

        writeLine("--- RECOMENDACIONES ---", headerPaint)
        writeLine("Café: ${boletin.recomendacionesCafe}", normalPaint)
        writeLine("Jamaica: ${boletin.recomendacionesJamaica}", normalPaint)
        writeLine("Hortalizas: ${boletin.recomendacionesHortalizas}", normalPaint)

        // Tabla diaria
        if (dias.isNotEmpty()) {
            writeLine("--- RESUMEN DIARIO ---", headerPaint)
            writeLine("Fecha    Tmin    Tmax    Precip (mm)", normalPaint)

            dias.forEach { d ->
                val line = "${d.date}    ${d.tMin?.let { "%.1f".format(it) } ?: "N/D"}    ${d.tMax?.let { "%.1f".format(it) } ?: "N/D"}    ${d.precip?.let { "%.1f".format(it) } ?: "0.0"}"
                writeLine(line, normalPaint)
            }
        }

        // Footer / página final
        pdf.finishPage(page)

        // Guardado en directorio app-visible
        val dir = File(context.getExternalFilesDir("pdfs"), "")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "boletin_${boletin.zona}_${System.currentTimeMillis()}.pdf")
        pdf.writeTo(FileOutputStream(file))
        pdf.close()
        return file
    }
}