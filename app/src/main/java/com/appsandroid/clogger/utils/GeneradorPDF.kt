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

        fun checkPageSpace(extra: Int = 30) {
            if (yPos > pageHeight - margin - extra) {
                pdf.finishPage(page)
                page = newPage(pdf.pages.size + 1)
                canvas = page.canvas
            }
        }

        fun writeLine(text: String, paint: Paint, addGap: Boolean = true) {
            val lines = text.split("\n")
            for (line in lines) {
                checkPageSpace()
                canvas.drawText(line, margin.toFloat(), yPos.toFloat(), paint)
                yPos += (paint.textSize + 8).toInt()
            }
            if (addGap) yPos += 6
        }

        /* ---------------------- HEADER ---------------------- */
        writeLine("BOLETÍN AGROCLIMÁTICO", titlePaint)
        writeLine("Zona: ${boletin.zona}", headerPaint)
        writeLine("Fecha de generación: ${boletin.fechaGeneracion}", headerPaint)
        yPos += 6

        /* ---------------------- RESUMEN ---------------------- */
        writeLine("--- RESUMEN CLIMÁTICO ---", headerPaint)
        writeLine(boletin.resumenClimatico, normalPaint)

        /* ---------------------- RECOMENDACIONES ---------------------- */
        writeLine("--- RECOMENDACIONES ---", headerPaint)
        writeLine("Café: ${boletin.recomendacionesCafe}", normalPaint)
        writeLine("Jamaica: ${boletin.recomendacionesJamaica}", normalPaint)
        writeLine("Hortalizas: ${boletin.recomendacionesHortalizas}", normalPaint)

        /* ---------------------- TABLA RESUMEN DIARIO ---------------------- */
        if (dias.isNotEmpty()) {
            writeLine("--- RESUMEN DIARIO ---", headerPaint)
            yPos += 8

            // Anchuras de columnas
            val colDate = margin + 0
            val colMin  = margin + 130
            val colMax  = margin + 220
            val colRain = margin + 310

            // Encabezados
            checkPageSpace()
            canvas.drawText("Fecha", colDate.toFloat(), yPos.toFloat(), headerPaint)
            canvas.drawText("Min °C", colMin.toFloat(), yPos.toFloat(), headerPaint)
            canvas.drawText("Max °C", colMax.toFloat(), yPos.toFloat(), headerPaint)
            canvas.drawText("Precip", colRain.toFloat(), yPos.toFloat(), headerPaint)
            yPos += 22

            // Filas
            dias.forEachIndexed { index, d ->
                checkPageSpace()

                val tmin = d.tMin?.let { "%.1f".format(it) } ?: "N/D"
                val tmax = d.tMax?.let { "%.1f".format(it) } ?: "N/D"
                val pr   = d.precip?.let { "%.1f".format(it) } ?: "0.0"

                canvas.drawText(d.date, colDate.toFloat(), yPos.toFloat(), normalPaint)
                canvas.drawText(tmin, colMin.toFloat(), yPos.toFloat(), normalPaint)
                canvas.drawText(tmax, colMax.toFloat(), yPos.toFloat(), normalPaint)
                canvas.drawText(pr, colRain.toFloat(), yPos.toFloat(), normalPaint)

                yPos += 18
            }

            yPos += 10
        }

        /* ---------------------- FOOTER ---------------------- */
        pdf.finishPage(page)

        // Guardar
        val dir = File(context.getExternalFilesDir("pdfs"), "")
        if (!dir.exists()) dir.mkdirs()
        val file = File(dir, "boletin_${boletin.zona}_${System.currentTimeMillis()}.pdf")

        pdf.writeTo(FileOutputStream(file))
        pdf.close()

        return file
    }
}