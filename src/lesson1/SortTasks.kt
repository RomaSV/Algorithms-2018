@file:Suppress("UNUSED_PARAMETER")

package lesson1

import java.io.File
import java.util.*
import kotlin.collections.ArrayList

fun <T> Array<T>.radixSort(maxElementLength: Int, bucketsCount: Int, digitAt: (T, Int) -> Int) {
    val buckets: Array<Queue<T>> = Array(bucketsCount) { LinkedList<T>() }

    var sorted = false
    var j = maxElementLength

    while (!sorted) {
        sorted = true
        for (element in this) {
            val bucket = digitAt(element, j)
            if (j >= 0) {
                sorted = false
                if (bucket > 0) buckets[bucket].add(element) else buckets[0].add(element)
            }
        }

        j--
        var i = 0
        for (bucket in buckets) {
            while (bucket.isNotEmpty()) {
                this[i++] = bucket.remove()
            }
        }
    }
}

/**
 * Сортировка времён
 *
 * Простая
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле с именем inputName содержатся моменты времени в формате ЧЧ:ММ:СС,
 * каждый на отдельной строке. Пример:
 *
 * 13:15:19
 * 07:26:57
 * 10:00:03
 * 19:56:14
 * 13:15:19
 * 00:40:31
 *
 * Отсортировать моменты времени по возрастанию и вывести их в выходной файл с именем outputName,
 * сохраняя формат ЧЧ:ММ:СС. Одинаковые моменты времени выводить друг за другом. Пример:
 *
 * 00:40:31
 * 07:26:57
 * 10:00:03
 * 13:15:19
 * 13:15:19
 * 19:56:14
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortTimes(inputName: String, outputName: String) {

    val format = Regex("""[0-2]\d:[0-6]\d:[0-6]\d""")
    val result = mutableListOf<String>()

    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) continue
        if (!line.matches(format)) {
            throw IllegalArgumentException("Date format is invalid. Should be '00:00:00' instead of '$line'")
        }
        result.add(line.filter { it != ':' })
    }

    val resultArray = result.toTypedArray()
    resultArray.radixSort(6, 10) { elem, index ->
        if (index >= 0 && index < elem.length) elem[index] - '0' else -1
    }

    File(outputName).bufferedWriter().use {
        for (line in resultArray.asList()) {
            it.write("${line.slice(0..1)}:${line.slice(2..3)}:${line.slice(4..5)}")
            it.newLine()
        }
    }
}

/**
 * Сортировка адресов
 *
 * Средняя
 *
 * Во входном файле с именем inputName содержатся фамилии и имена жителей города с указанием улицы и номера дома,
 * где они прописаны. Пример:
 *
 * Петров Иван - Железнодорожная 3
 * Сидоров Петр - Садовая 5
 * Иванов Алексей - Железнодорожная 7
 * Сидорова Мария - Садовая 5
 * Иванов Михаил - Железнодорожная 7
 *
 * Людей в городе может быть до миллиона.
 *
 * Вывести записи в выходной файл outputName,
 * упорядоченными по названию улицы (по алфавиту) и номеру дома (по возрастанию).
 * Людей, живущих в одном доме, выводить через запятую по алфавиту (вначале по фамилии, потом по имени). Пример:
 *
 * Железнодорожная 3 - Петров Иван
 * Железнодорожная 7 - Иванов Алексей, Иванов Михаил
 * Садовая 5 - Сидоров Петр, Сидорова Мария
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun sortAddresses(inputName: String, outputName: String) {
    val format = Regex("""[А-ЯЁ][а-яё]+ [А-ЯЁ][а-яё]+ - [А-ЯЁ][а-яё]+ \d+""")
    val addressMap = mutableMapOf<String, ArrayList<String>>()
    var maxAddressLen = 0
    var maxNameLen = 0

    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) continue
        if (!line.matches(format)) {
            throw IllegalArgumentException("Input format is invalid.")
        }

        val data = line.split(" - ")
        val list = addressMap.getOrDefault(data[1], arrayListOf())
        list.add(data[0])
        addressMap[data[1]] = list
        if (data[0].length > maxNameLen) maxNameLen = data[1].length
        if (data[1].length > maxAddressLen) maxAddressLen = data[1].length
    }


    val addresses = addressMap.keys.toTypedArray()
    //sort by numbers
    addresses.radixSort(maxAddressLen / 2, 10) { elem, index ->
        val filteredString = elem.replace("[А-Яа-я ]+".toRegex(), "")
        if (index >= 0 && index < filteredString.length) filteredString[index] - '0' else -1
    }
    // sort by letters
    addresses.radixSort(maxAddressLen, 32) { elem, index -> letterInAddressAt(elem, index) }

    File(outputName).bufferedWriter().use {
        for (address in addresses) {
            val people = addressMap[address]!!.toTypedArray()
            people.radixSort(maxNameLen, 33) { elem, index -> letterInNameAt(elem, index) }

            it.write("$address - ${people.asList().joinToString()}")
            it.newLine()
        }
    }
}

fun letterInAddressAt(str: String, index: Int): Int {
    val filteredString = str.filter { it !in "0123456789 " }.toLowerCase()
    return if (index >= 0 && index < filteredString.length) filteredString[index] - 'а' else -1
}

fun letterInNameAt(str: String, index: Int): Int {
    val filteredString = str.toLowerCase()
    return if (index >= 0 && index < filteredString.length) {
        if (filteredString[index] != ' ') filteredString[index] - 'а' + 1 else 0
    } else {
        -1
    }
}


/**
 * Сортировка температур
 *
 * Средняя
 * (Модифицированная задача с сайта acmp.ru)
 *
 * Во входном файле заданы температуры различных участков абстрактной планеты с точностью до десятых градуса.
 * Температуры могут изменяться в диапазоне от -273.0 до +500.0.
 * Например:
 *
 * 24.7
 * -12.6
 * 121.3
 * -98.4
 * 99.5
 * -12.6
 * 11.0
 *
 * Количество строк в файле может достигать ста миллионов.
 * Вывести строки в выходной файл, отсортировав их по возрастанию температуры.
 * Повторяющиеся строки сохранить. Например:
 *
 * -98.4
 * -12.6
 * -12.6
 * 11.0
 * 24.7
 * 99.5
 * 121.3
 */
fun sortTemperatures(inputName: String, outputName: String) {

    val format = Regex("""-?\d+.\d+""")
    val firstBucketValue = -2730
    val buckets = Array(7740) { 0 }

    for (line in File(inputName).readLines()) {
        if (line.isEmpty()) continue
        if (!line.matches(format)) {
            throw IllegalArgumentException("Temperature format is invalid.")
        }
        buckets[line.filter { it != '.' }.toInt() - firstBucketValue]++
    }

    File(outputName).bufferedWriter().use {
        for ((bucketIndex, bucket) in buckets.withIndex()) {
            for (i in 0 until bucket) {
                val integerPart = (bucketIndex + firstBucketValue) / 10
                val dotPart = (bucketIndex + firstBucketValue) % 10
                if (integerPart == 0 && dotPart < 0) it.write("-")
                it.write("$integerPart.${Math.abs(dotPart)}")
                it.newLine()
            }
        }
    }
}

/**
 * Сортировка последовательности
 *
 * Средняя
 * (Задача взята с сайта acmp.ru)
 *
 * В файле задана последовательность из n целых положительных чисел, каждое в своей строке, например:
 *
 * 1
 * 2
 * 3
 * 2
 * 3
 * 1
 * 2
 *
 * Необходимо найти число, которое встречается в этой последовательности наибольшее количество раз,
 * а если таких чисел несколько, то найти минимальное из них,
 * и после этого переместить все такие числа в конец заданной последовательности.
 * Порядок расположения остальных чисел должен остаться без изменения.
 *
 * 1
 * 3
 * 3
 * 1
 * 2
 * 2
 * 2
 */
fun sortSequence(inputName: String, outputName: String) {
    TODO()
}

/**
 * Соединить два отсортированных массива в один
 *
 * Простая
 *
 * Задан отсортированный массив first и второй массив second,
 * первые first.size ячеек которого содержат null, а остальные ячейки также отсортированы.
 * Соединить оба массива в массиве second так, чтобы он оказался отсортирован. Пример:
 *
 * first = [4 9 15 20 28]
 * second = [null null null null null 1 3 9 13 18 23]
 *
 * Результат: second = [1 3 4 9 9 13 15 20 23 28]
 */
fun <T : Comparable<T>> mergeArrays(first: Array<T>, second: Array<T?>) {
    TODO()
}

