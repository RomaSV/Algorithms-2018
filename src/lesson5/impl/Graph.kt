package lesson5.impl

import lesson5.Graph.Edge
import lesson5.Graph
import lesson5.Graph.Vertex

class GraphBuilder {

    data class VertexImpl(private val nameField: String) : Vertex {
        override fun getName() = nameField

        override fun toString() = name
    }

    data class EdgeImpl(private val _begin: Vertex,
                        private val _end: Vertex,
                        private val weightField: Int = 1) : Edge {
        override fun getBegin() = _begin

        override fun getEnd() = _end

        override fun getWeight() = weightField

        // Т.к. графы в задачах неориентированные - считаем равными ребра (a,b) и (b,a)
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || javaClass != other.javaClass) return false
            val that = other as EdgeImpl
            return (begin == that.begin && end == that.end) || (begin == that.end && end == that.begin)
        }

        override fun hashCode(): Int {
            return 31 * (_begin.hashCode() + _end.hashCode())
        }

    }

    private val vertices = mutableMapOf<String, Vertex>()

    private val connections = mutableMapOf<Vertex, Set<EdgeImpl>>()

    private fun addVertex(v: Vertex) {
        vertices[v.name] = v
    }

    fun addVertex(name: String): Vertex {
        return VertexImpl(name).apply {
            addVertex(this)
        }
    }

    fun addConnection(begin: Vertex, end: Vertex, weight: Int = 1) {
        val edge = EdgeImpl(begin, end, weight)
        connections[begin] = connections[begin]?.let { it + edge } ?: setOf(edge)
        connections[end] = connections[end]?.let { it + edge } ?: setOf(edge)
    }

    fun build(): Graph = object : Graph {

        override fun get(name: String): Vertex? = this@GraphBuilder.vertices[name]

        override fun getVertices(): Set<Vertex> = this@GraphBuilder.vertices.values.toSet()

        override fun getEdges(): Set<Edge> {
            return connections.values.flatten().toSet()
        }

        override fun getConnections(v: Vertex): Map<Vertex, Edge> {
            val edges = connections[v] ?: emptySet()
            val result = mutableMapOf<Vertex, Edge>()
            for (edge in edges) {
                when (v) {
                    edge.begin -> result[edge.end] = edge
                    edge.end -> result[edge.begin] = edge
                }
            }
            return result
        }
    }
}