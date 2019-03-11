package com.liyuanhust.pdfviewer

import android.content.Context
import android.os.Handler
import android.util.Log
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*


class FileLoader(private val context: Context) {

    private var isCancle = false
    private var call:Call? = null

    private var handler:Handler? = Handler()

    fun startDownload(url:String, completer: ((File?)->Unit)?){
        okHttpClient.dispatcher().executorService().execute {
            val file = loadFile(url)
            handler?.post {
                completer?.invoke(file)
            }
        }
    }

    fun cancel(){
        log("do cancel")
        isCancle = true
        call?.cancel()
        handler?.removeCallbacksAndMessages(null)
    }


    private fun loadFile(url:String?):File?{
        var pdfFile:File? = null
        var tempFile:File? = null
        var inputStream:InputStream? = null
        var fileOutputStream:FileOutputStream? = null

        fun InputStream.copyTo(out: OutputStream, bufferSize: Int = DEFAULT_BUFFER_SIZE): Long {
            var bytesCopied: Long = 0
            val buffer = ByteArray(bufferSize)
            var bytes = read(buffer)
            while (bytes >= 0 && !isCancle) {
                out.write(buffer, 0, bytes)
                bytesCopied += bytes
                bytes = read(buffer)
            }
            return bytesCopied
        }


        fun checkPdfFile():File? {
            if (url.isNullOrEmpty()) {
                log("url is empty")
                return null
            }
            val md5 = getMD5(url.orEmpty())
            if (md5.isEmpty()) {
                log("get md5 failed, url:${url}")
                return null
            }

            //Create file
            val fileName = "pdfcache_${md5}.pdf"
            val cacheFolder = File(context.externalCacheDir?:context.cacheDir, "pdfs")
            cacheFolder.mkdirs()
            return File(cacheFolder, fileName)
        }

        fun downloadToTemp(){
            if (isCancle) {
                throw RuntimeException("downloadToTemp canceled!")
            }
            val tempFileName = UUID.randomUUID().toString()
            val downloadFolder = File(context.externalCacheDir?:context.cacheDir, "downloads")
            downloadFolder.mkdirs()
            tempFile = File(downloadFolder, tempFileName)
            if (isCancle) return

            val request = Request.Builder().url(url.orEmpty()).build()
            val call =  okHttpClient.newCall(request)
            this.call = call
            inputStream = call.execute().body()?.byteStream()
            fileOutputStream = FileOutputStream(tempFile)
            inputStream?.copyTo(FileOutputStream(tempFile))
            fileOutputStream?.close()
            inputStream?.close()
            if (isCancle) {
                throw RuntimeException("downloadToTemp canceled!")
            }
            if (tempFile?.exists() != true) {
                log("download file failed!, url:${url}, tempfile:${tempFile?.path}")
                throw RuntimeException("downloadToTemp canceled!")
            }
        }

        fun copyFile(){
            if (isCancle) {
                throw RuntimeException("copyFile canceled!")
            }
            tempFile?.copyTo(pdfFile!!)
            if (pdfFile?.length() != tempFile?.length()) {
                log("copy file failed!")
                pdfFile?.delete()
                tempFile?.delete()
            }
        }


        fun closeAll(){
            try {
                inputStream?.close()
                fileOutputStream?.close()
                tempFile?.delete()
            } catch (e:Throwable) {
                log("closeAll failed, ${e.message}")
            }
        }

        try {
            pdfFile = checkPdfFile()
            if (pdfFile == null) {
                log("create pdf file failed!")
                return null
            }
            if (pdfFile.exists()) {
                log("pre file exist")
                return pdfFile
            }
            downloadToTemp()
            copyFile()
        } catch (e:Throwable) {
            log("load file failed! ${e.message}")
        }finally {
            closeAll()
        }

        return pdfFile
    }

    private fun log(info:String){
        Log.w("FileDownloader", info)
    }

    companion object {
        private val okHttpClient = OkHttpClient.Builder().build()

        private fun getMD5(str: String): String {
            try {
                // 生成一个MD5加密计算摘要
                val md = MessageDigest.getInstance("MD5")
                // 计算md5函数
                md.update(str.toByteArray())
                // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
                // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
                return BigInteger(1, md.digest()).toString(16)
            } catch (e: Exception) {
                return ""
            }

        }
    }

}