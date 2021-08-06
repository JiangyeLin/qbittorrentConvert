import java.io.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.LinkedBlockingDeque
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

private val executorService: ExecutorService = ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, LinkedBlockingDeque())


private var outputFile: File? = null

private var count = 0
fun main(args: Array<String>) {
    println("Hello World!")

    val path = "C:\\Users\\JiangyeLin\\Downloads\\qbittorrent\\profile\\qBittorrent\\data\\BT_backup"

    outputFile = File(path + File.separator + "out")
    if (!outputFile?.exists()!!) {
        outputFile?.mkdirs()
    }

    loadFile(path)
    //C:\Users\JiangyeLin\Downloads\qbittorrent\profile\qBittorrent\data\BT_backup
}

data class Config(val state: Int)

fun loadFile(path: String) {
    val file = File(path)
    if (!file.exists()) {
        println("文件不存在")
        return
    }

    println("原目录大小：${file.list().size}")
    val files = file.list { dir, name ->
        name.contains("fastresume")
    }

    println("筛选后大小：${files.size}")

    files.forEach {
        executorService.submit {
            convert(file.absolutePath + File.separator + it)
        }
    }
}

fun convert(path: String) {
    println("开始转换  $path")
    val file = File(path)
    if (!file.exists()) {
        return
    }

    val inputStreamReader = InputStreamReader(file.inputStream())
    val bufferedReader = BufferedReader(inputStreamReader)

    var lineTxt: String? = null

    val stringBuilder = StringBuilder()
    while (bufferedReader.readLine().also { lineTxt = it } != null) {
        stringBuilder.append(lineTxt)
    }
    bufferedReader.close()
    inputStreamReader.close()
    output(stringBuilder.toString(), file.name)
}

fun output(string: String, name: String) {
    val file = File(outputFile, name)
    println("输出路径：${file.absolutePath}")

    val outputStreamWriter = OutputStreamWriter(file.outputStream())
    val bufferedWriter = BufferedWriter(outputStreamWriter)

    var result: String = ""
    if (string.contains("D:\\Movies")) {
        string.replace("D:\\Movies", "C:\\Test")
    }
    if (string.contains("")) {

    }
    bufferedWriter.write(result, 0, result.length)
    bufferedWriter.flush()

    bufferedWriter.close()
    outputStreamWriter.close()

    count++
    println("写入完成  $count")
}




