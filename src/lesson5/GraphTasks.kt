@file:Suppress("UNUSED_PARAMETER", "unused")

package lesson5

import lesson5.impl.GraphBuilder
import java.util.*

/**
 * Эйлеров цикл.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему любой Эйлеров цикл.
 * Если в графе нет Эйлеровых циклов, вернуть пустой список.
 * Соседние дуги в списке-результате должны быть инцидентны друг другу,
 * а первая дуга в списке инцидентна последней.
 * Длина списка, если он не пуст, должна быть равна количеству дуг в графе.
 * Веса дуг никак не учитываются.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Вариант ответа: A, E, J, K, D, C, H, G, B, C, I, F, B, A
 *
 * Справка: Эйлеров цикл -- это цикл, проходящий через все рёбра
 * связного графа ровно по одному разу
 */
fun Graph.findEulerLoop(): List<Graph.Edge> {
    if (!this.mayContainEulerLoop()) return listOf()

    val result = mutableListOf<Graph.Edge>()
    val resultVertices = Stack<Graph.Vertex>()

    val edges = this.edges
    val vertices = this.vertices
    val verticesStack = Stack<Graph.Vertex>()
    verticesStack.push(vertices.first())

    while (verticesStack.isNotEmpty()) {
        val current = verticesStack.peek()
        for (vertex in vertices) {
            val edge = GraphBuilder.EdgeImpl(current, vertex)
            if (edges.contains(edge)) {
                verticesStack.push(vertex)
                edges.remove(edge)
                break
            }
        }
        if (current == verticesStack.peek()) {
            verticesStack.pop()
            if (resultVertices.isNotEmpty()) {
                result.add(GraphBuilder.EdgeImpl(resultVertices.peek(), current))
            }
            resultVertices.push(current)
        }
    }

    return result
}

fun Graph.mayContainEulerLoop(): Boolean {
    for (vertex in this.vertices) {
        if (this.getNeighbors(vertex).size % 2 != 0) return false
    }
    return true
}

/**
 * Минимальное остовное дерево.
 * Средняя
 *
 * Дан граф (получатель). Найти по нему минимальное остовное дерево.
 * Если есть несколько минимальных остовных деревьев с одинаковым числом дуг,
 * вернуть любое из них. Веса дуг не учитывать.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ:
 *
 *      G    H
 *      |    |
 * A -- B -- C -- D
 * |    |    |
 * E    F    I
 * |
 * J ------------ K
 */
fun Graph.minimumSpanningTree(): Graph {
    TODO()
}

/**
 * Максимальное независимое множество вершин в графе без циклов.
 * Сложная
 *
 * Дан граф без циклов (получатель), например
 *
 *      G -- H -- J
 *      |
 * A -- B -- D
 * |         |
 * C -- F    I
 * |
 * E
 *
 * Найти в нём самое большое независимое множество вершин и вернуть его.
 * Никакая пара вершин в независимом множестве не должна быть связана ребром.
 *
 * Если самых больших множеств несколько, приоритет имеет то из них,
 * в котором вершины расположены раньше во множестве this.vertices (начиная с первых).
 *
 * В данном случае ответ (A, E, F, D, G, J)
 *
 * Эта задача может быть зачтена за пятый и шестой урок одновременно
 */
fun Graph.largestIndependentVertexSet(): Set<Graph.Vertex> {
    val calculatedIndSets = mutableMapOf<Graph.Vertex, Set<Graph.Vertex>>()
    return this.largestIndependentVertexSet(this.vertices.first(), null, calculatedIndSets)
}

fun Graph.largestIndependentVertexSet(
        vertex: Graph.Vertex, parent: Graph.Vertex?,
        calculated: MutableMap<Graph.Vertex, Set<Graph.Vertex>>): Set<Graph.Vertex> {

    return calculated.getOrElse(vertex) {
        var childrenSum = setOf<Graph.Vertex>()
        var grandchildrenSum = setOf(vertex)

        for (children in this.getNeighbors(vertex)) {
            if (children == parent) continue
            childrenSum += this.largestIndependentVertexSet(children, vertex, calculated)

            for (grandchildren in this.getNeighbors(children)) {
                if (grandchildren == vertex) continue
                grandchildrenSum += this.largestIndependentVertexSet(grandchildren, children, calculated)
            }
        }
        val result = if (grandchildrenSum.size >= childrenSum.size) grandchildrenSum else childrenSum
        calculated[vertex] = result
        return result
    }
}

/**
 * Наидлиннейший простой путь.
 * Сложная
 *
 * Дан граф (получатель). Найти в нём простой путь, включающий максимальное количество рёбер.
 * Простым считается путь, вершины в котором не повторяются.
 * Если таких путей несколько, вернуть любой из них.
 *
 * Пример:
 *
 *      G -- H
 *      |    |
 * A -- B -- C -- D
 * |    |    |    |
 * E    F -- I    |
 * |              |
 * J ------------ K
 *
 * Ответ: A, E, J, K, D, C, H, G, B, F, I
 */
fun Graph.longestSimplePath(): Path {
    TODO()
}