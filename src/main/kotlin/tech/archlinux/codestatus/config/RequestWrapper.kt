package tech.archlinux.codestatus.config

import jakarta.servlet.ReadListener
import jakarta.servlet.ServletInputStream
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletRequestWrapper
import java.io.*

class RequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    /**
     * 存储请求数据
     */
    private var body: String

    init {
        val stringBuilder = StringBuilder()
        val bufferedReader = try {
            val inputStream: InputStream = request.inputStream
            val bf = BufferedReader(InputStreamReader(inputStream))
            val charBuffer = CharArray(128)
            var bytesRead: Int
            while (bf.read(charBuffer).also { bytesRead = it } > 0) {
                stringBuilder.appendRange(charBuffer, 0, bytesRead)
            }
            bf
        } catch (ex: IOException) {
            ex.printStackTrace()
            null
        }
        bufferedReader?.let {
            try {
                it.close()
            } catch (ex: IOException) {
                ex.printStackTrace()
            }
        }
        body = stringBuilder.toString()
    }

    /**
     * 重写getInputStream方法
     */
    override fun getInputStream(): ServletInputStream {
        val byteArrayInputStream = ByteArrayInputStream(body.toByteArray())
        return object : ServletInputStream() {
            override fun isFinished(): Boolean {
                return false
            }

            override fun isReady(): Boolean {
                return false
            }

            override fun setReadListener(readListener: ReadListener) {}
            override fun read(): Int {
                return byteArrayInputStream.read()
            }
        }
    }

    override fun getReader(): BufferedReader {
        return BufferedReader(InputStreamReader(this.inputStream))
    }
}
