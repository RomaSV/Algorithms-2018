package lesson3

import java.util.*
import kotlin.NoSuchElementException

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>>() : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private constructor(node: Node<T>, lowest: T?, highest: T?) : this() {
        root = node
        this.lowest = lowest
        this.highest = highest
    }

    private var root: Node<T>? = null

    override var size = 0
        private set
        get() {
            var result = 0
            for (node in this) {
                result++
            }
            return result
        }

    /**
     * The lowest value that can be added in that tree.
     * Unlimited if null.
     */
    private var lowest: T? = null

    /**
     * The highest value that can be added in that tree.
     * Unlimited if null.
     */
    private var highest: T? = null


    private class Node<T>(val value: T) {

        var parent: Node<T>? = null

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        if (highest != null && highest!! <= element)
            throw IllegalArgumentException("Element $element is higher than the upper limit of this set.")
        if (lowest != null && lowest!! > element)
            throw IllegalArgumentException("Element $element is lower than the lower limit of this set.")

        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
        newNode.parent = closest
        when {
            closest == null -> root = newNode
            comparison < 0 -> {
                assert(closest.left == null)
                closest.left = newNode
            }
            else -> {
                assert(closest.right == null)
                closest.right = newNode
            }
        }
        return true
    }

    override fun checkInvariant(): Boolean =
            root?.let { checkInvariant(it) } ?: true

    private fun checkInvariant(node: Node<T>): Boolean {
        val left = node.left
        if (left != null && (left.value >= node.value || !checkInvariant(left))) return false
        val right = node.right
        return right == null || right.value > node.value && checkInvariant(right)
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     *
     * Сложность: O(h), где h - высота дерева
     */
    override fun remove(element: T): Boolean {
        val closest = find(element)
        if (closest == null || element.compareTo(closest.value) != 0) return false
        return remove(closest)
    }

    private fun remove(node: Node<T>): Boolean {

        val parent = node.parent

        when {
            node.left == null && node.right == null -> parent.replaceChild(node, null)
            node.left == null -> parent.replaceChild(node, node.right)
            node.right == null -> parent.replaceChild(node, node.left)
            else -> {
                var change = node.right
                while (true) {
                    if (change!!.left == null) break
                    change = change.left
                }

                val replacement = Node(change!!.value)
                replacement.left = if (replacement.value != node.left?.value) node.left else null
                replacement.right = if (replacement.value != node.right?.value) node.right else node.right?.right
                parent.replaceChild(node, replacement)

                change.parent.replaceChild(change, change.right)
            }
        }

        return true
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? {
        if (highest != null && highest!! <= value) return null
        if (lowest != null && lowest!! > value) return null
        return root?.let { find(it, value) }
    }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    private fun Node<T>?.replaceChild(node: Node<T>, newNode: Node<T>?) {
        newNode?.parent = this

        when {
            this == null -> root = newNode
            this.left?.value?.compareTo(node.value) == 0 -> this.left = newNode
            else -> this.right = newNode
        }
    }

    inner class BinaryTreeIterator : MutableIterator<T> {

        private var current: Node<T>? = null
        private var stack: Stack<Node<T>> = Stack()

        init {
            var node = root
            while (node != null) {
                stack.push(node)
                node = node.left
            }
        }

        /**
         * Поиск следующего элемента
         * Средняя
         *
         * Сложность: в среднем - O(1); в худшем случае - O(h), где h - высота дерева
         * Память: O(h)
         */
        private fun findNext(): Node<T> {

            var node: Node<T>? = stack.pop()
            val result: Node<T> = node!!

            node = node.right

            if (highest != null && node != null && node.value >= highest!!) {
                while (node?.left != null) {
                    node = node.left
                    if (node!!.value < highest!!) break
                }
            }

            while (node != null) {
                stack.push(node)
                node = node.left
            }

            return result
        }

        override fun hasNext(): Boolean = when {
            stack.isEmpty() -> false
            highest != null && stack.peek().value >= highest!! -> false
            else -> true
        }

        override fun next(): T {
            if (!hasNext()) throw NoSuchElementException()
            current = findNext()
            return current!!.value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            remove(current ?: return)
        }
    }

    override fun iterator(): MutableIterator<T> = BinaryTreeIterator()

    override fun comparator(): Comparator<in T>? = null

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */
    override fun subSet(fromElement: T, toElement: T): SortedSet<T> {
        TODO()
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */
    override fun headSet(toElement: T): SortedSet<T> {
        val result: KtBinaryTree<T>

        if (root != null) {
            var headSetRoot: Node<T> = root!!
            while (toElement < headSetRoot.value) {
                headSetRoot = headSetRoot.left ?: break
            }
            result = KtBinaryTree(headSetRoot, null, toElement)
        } else {
            result = KtBinaryTree()
        }

        return result
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */
    override fun tailSet(fromElement: T): SortedSet<T> {
        TODO()
    }

    override fun first(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.left != null) {
            current = current.left!!
        }
        return current.value
    }

    override fun last(): T {
        var current: Node<T> = root ?: throw NoSuchElementException()
        while (current.right != null) {
            current = current.right!!
        }
        return current.value
    }
}
