package lesson3

import java.util.SortedSet
import kotlin.NoSuchElementException

// Attention: comparable supported but comparator is not
class KtBinaryTree<T : Comparable<T>> : AbstractMutableSet<T>(), CheckableSortedSet<T> {

    private var root: Node<T>? = null

    override var size = 0
        private set

    private class Node<T>(val value: T) {

        var left: Node<T>? = null

        var right: Node<T>? = null
    }

    override fun add(element: T): Boolean {
        val closest = find(element)
        val comparison = if (closest == null) -1 else element.compareTo(closest.value)
        if (comparison == 0) {
            return false
        }
        val newNode = Node(element)
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
        size++
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
        val pair = findWithParent(element)
        val closest = pair?.second
        if (closest == null || element.compareTo(closest.value) != 0) return false

        val parent = pair.first

        when {
            closest.left == null && closest.right == null -> parent.replaceChild(closest, null)
            closest.left == null -> parent.replaceChild(closest, closest.right)
            closest.right == null -> parent.replaceChild(closest, closest.left)
            else -> {
                var change = closest.right
                var changeParent = closest
                while (true) {
                    if (change!!.left == null) break
                    changeParent = change
                    change = change.left
                }

                val replacement = Node(change!!.value)

                if (closest.left != null && replacement.value != closest.left!!.value) {
                    replacement.left = closest.left
                } else {
                    replacement.left = null
                }
                if (closest.right != null && replacement.value != closest.right!!.value) {
                    replacement.right = closest.right
                } else if (closest.right != null && closest.right!!.right != null) {
                    replacement.right = closest.right!!.right
                } else {
                    replacement.right = null
                }

                parent.replaceChild(closest, replacement)

                if (change.right != null) changeParent!!.replaceChild(change, change.right) else changeParent!!.replaceChild(change, null)
            }
        }

        size--
        return true
    }

    override operator fun contains(element: T): Boolean {
        val closest = find(element)
        return closest != null && element.compareTo(closest.value) == 0
    }

    private fun find(value: T): Node<T>? =
            root?.let { find(it, value) }

    private fun find(start: Node<T>, value: T): Node<T> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> start
            comparison < 0 -> start.left?.let { find(it, value) } ?: start
            else -> start.right?.let { find(it, value) } ?: start
        }
    }

    private fun findWithParent(value: T): Pair<Node<T>?, Node<T>>? {
        if (root == null) return null
        if (root!!.value.compareTo(value) == 0) return Pair(null, root!!)
        return root?.let { findWithParent(root, it, value) }
    }


    private fun findWithParent(parent: Node<T>?, start: Node<T>, value: T): Pair<Node<T>?, Node<T>> {
        val comparison = value.compareTo(start.value)
        return when {
            comparison == 0 -> Pair(parent, start)
            comparison < 0 -> start.left?.let { findWithParent(start, it, value) } ?: Pair(parent, start)
            else -> start.right?.let { findWithParent(start, it, value) } ?: Pair(parent, start)
        }
    }

    private fun Node<T>?.replaceChild(node: Node<T>, newNode: Node<T>?) {
        when {
            this == null -> root = newNode
            this.left != null && this.left!!.value.compareTo(node.value) == 0 -> this.left = newNode
            else -> this.right = newNode
        }
    }

    inner class BinaryTreeIterator : MutableIterator<T> {

        private var current: Node<T>? = null

        /**
         * Поиск следующего элемента
         * Средняя
         */
        private fun findNext(): Node<T>? {
            return null // TODO
        }

        override fun hasNext(): Boolean = findNext() != null

        override fun next(): T {
            current = findNext()
            return (current ?: throw NoSuchElementException()).value
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        override fun remove() {
            return // TODO
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
        TODO()
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
