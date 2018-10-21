@file:Suppress("UNUSED_PARAMETER")

package lesson2

import lesson3.Trie
import java.io.File

/**
 * Получение наибольшей прибыли (она же -- поиск максимального подмассива)
 * Простая
 *
 * Во входном файле с именем inputName перечислены цены на акции компании в различные (возрастающие) моменты времени
 * (каждая цена идёт с новой строки). Цена -- это целое положительное число. Пример:
 *
 * 201
 * 196
 * 190
 * 198
 * 187
 * 194
 * 193
 * 185
 *
 * Выбрать два момента времени, первый из них для покупки акций, а второй для продажи, с тем, чтобы разница
 * между ценой продажи и ценой покупки была максимально большой. Второй момент должен быть раньше первого.
 * Вернуть пару из двух моментов.
 * Каждый момент обозначается целым числом -- номер строки во входном файле, нумерация с единицы.
 * Например, для приведённого выше файла результат должен быть Pair(3, 4)
 *
 * В случае обнаружения неверного формата файла бросить любое исключение.
 */
fun optimizeBuyAndSell(inputName: String): Pair<Int, Int> {
    TODO()
}

/**
 * Задача Иосифа Флафия.
 * Простая
 *
 * Образовав круг, стоят menNumber человек, пронумерованных от 1 до menNumber.
 *
 * 1 2 3
 * 8   4
 * 7 6 5
 *
 * Мы считаем от 1 до choiceInterval (например, до 5), начиная с 1-го человека по кругу.
 * Человек, на котором остановился счёт, выбывает.
 *
 * 1 2 3
 * 8   4
 * 7 6 х
 *
 * Далее счёт продолжается со следующего человека, также от 1 до choiceInterval.
 * Выбывшие при счёте пропускаются, и человек, на котором остановился счёт, выбывает.
 *
 * 1 х 3
 * 8   4
 * 7 6 Х
 *
 * Процедура повторяется, пока не останется один человек. Требуется вернуть его номер (в данном случае 3).
 *
 * 1 Х 3
 * х   4
 * 7 6 Х
 *
 * 1 Х 3
 * Х   4
 * х 6 Х
 *
 * х Х 3
 * Х   4
 * Х 6 Х
 *
 * Х Х 3
 * Х   х
 * Х 6 Х
 *
 * Х Х 3
 * Х   Х
 * Х х Х
 */
fun josephTask(menNumber: Int, choiceInterval: Int): Int {
    TODO()
}

/**
 * Наибольшая общая подстрока.
 * Средняя
 *
 * Дано две строки, например ОБСЕРВАТОРИЯ и КОНСЕРВАТОРЫ.
 * Найти их самую длинную общую подстроку -- в примере это СЕРВАТОР.
 * Если общих подстрок нет, вернуть пустую строку.
 * При сравнении подстрок, регистр символов *имеет* значение.
 * Если имеется несколько самых длинных общих подстрок одной длины,
 * вернуть ту из них, которая встречается раньше в строке first.
 *
 * Сложность: O(nm), где n и m - длины исходных строк
 * Память: O(nm)
 */
fun longestCommonSubstring(first: String, second: String): String {
    val similarityTable = Array(first.length) { IntArray(second.length) }
    var maxLen = 0
    var maxIndex = -1
    for (i in 0 until first.length) {
        for (j in 0 until second.length) {
            if (first[i] == second[j]) {
                if (i > 0 && j > 0) {
                    val len = similarityTable[i - 1][j - 1] + 1
                    if (len > maxLen) {
                        maxLen = len
                        maxIndex = i
                    }
                    similarityTable[i][j] = len
                } else {
                    similarityTable[i][j] = 1
                }
            }
        }
    }

    if (maxLen == 0) return ""

    val result = StringBuilder()
    for (i in maxLen - 1 downTo 0) {
        result.append(first[maxIndex - i])
    }

    return result.toString()
}

/**
 * Число простых чисел в интервале
 * Простая
 *
 * Рассчитать количество простых чисел в интервале от 1 до limit (включительно).
 * Если limit <= 1, вернуть результат 0.
 *
 * Справка: простым считается число, которое делится нацело только на 1 и на себя.
 * Единица простым числом не считается.
 */
fun calcPrimesNumber(limit: Int): Int {
    TODO()
}

/**
 * Балда
 * Сложная
 *
 * В файле с именем inputName задана матрица из букв в следующем формате
 * (отдельные буквы в ряду разделены пробелами):
 *
 * И Т Ы Н
 * К Р А Н
 * А К В А
 *
 * В аргументе words содержится множество слов для поиска, например,
 * ТРАВА, КРАН, АКВА, НАРТЫ, РАК.
 *
 * Попытаться найти каждое из слов в матрице букв, используя правила игры БАЛДА,
 * и вернуть множество найденных слов. В данном случае:
 * ТРАВА, КРАН, АКВА, НАРТЫ
 *
 * И т Ы Н     И т ы Н
 * К р а Н     К р а н
 * А К в а     А К В А
 *
 * Все слова и буквы -- русские или английские, прописные.
 * В файле буквы разделены пробелами, строки -- переносами строк.
 * Остальные символы ни в файле, ни в словах не допускаются.
 */
fun baldaSearcher(inputName: String, words: Set<String>): Set<String> {

    val format = Regex("""[А-ЯЁA-Z ]+""")
    val wordMatrix = ArrayList<Char>()
    val wordsTrie = Trie()
    val result = HashSet<String>()

    var width = 0
    var height = 0

    for (word in words) {
        wordsTrie.add(word)
    }

    for (line in File(inputName).readLines()) {
        if (!line.matches(format)) {
            throw IllegalArgumentException("Input format is invalid.")
        }
        val chars = line.split(" ")
        if (width == 0) width = chars.size
        height++

        chars.forEach { char -> wordMatrix.add(char.first()) }
    }

    for (index in 0 until wordMatrix.size) {
        result.addAll(findWordsAt(index, wordMatrix, width, height, wordsTrie))
    }

    return result
}

fun findWordsAt(index: Int, wordMatrix: List<Char>, width: Int, height: Int, words: Trie): Set<String> =
        findWordsAt(index, wordMatrix, width, height, words, "", listOf())

private fun findWordsAt(index: Int, wordMatrix: List<Char>, width: Int, height: Int, words: Trie,
                        letters: String, visited: List<Int>): Set<String> {

    val foundWords = HashSet<String>()

    if (index !in visited) {
        val thisLetters = letters + wordMatrix[index]
        var visitedList = visited
        visitedList += index

        if (words.containsPrefix(thisLetters)) {

            if (words.contains(thisLetters)) {
                foundWords.add(thisLetters)
            }

            if (getX(index, width) > 0) {
                foundWords.addAll(findWordsAt(index - 1, wordMatrix, width, height, words, thisLetters, visitedList))
            }
            if (getX(index, width) < width - 1) {
                foundWords.addAll(findWordsAt(index + 1, wordMatrix, width, height, words, thisLetters, visitedList))
            }
            if (getY(index, width) > 0) {
                foundWords.addAll(findWordsAt(index - width, wordMatrix, width, height, words, thisLetters, visitedList))
            }
            if (getY(index, width) < height - 1) {
                foundWords.addAll(findWordsAt(index + width, wordMatrix, width, height, words, thisLetters, visitedList))
            }
        }
    }

    return foundWords
}

private fun getX(index: Int, width: Int): Int {
    return index % width
}

private fun getY(index: Int, width: Int): Int {
    return Math.floorDiv(index, width)
}