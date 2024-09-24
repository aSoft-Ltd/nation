package nation.svg

import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import org.w3c.dom.Node
import org.w3c.dom.NodeList

class XMLTokenizer(private val file: File) {
    fun tokenize(): Tag.Svg {
        val factory = DocumentBuilderFactory.newInstance()
        val builder = factory.newDocumentBuilder()
        val document = builder.parse(file)
        val svg = document.getElementsByTagName("svg").item(0)
        val box = svg.attributes.getNamedItem("viewBox").nodeValue.split(" ").map { it.toInt() }
        val tag = Tag.Svg(
            view = ViewBox(box[0], box[1], box[2], box[3]),
            content = svg.childNodes.mapNotNull { it.toTag() }
        )
        return tag
    }

    sealed interface Tag {
        data class Svg(val view: ViewBox, val content: List<Tag>) : Tag
        data class Group(val children: List<Tag>) : Tag
        data class Path(val d: String) : Tag
    }

    fun <R> NodeList.mapNotNull(transform: (Node) -> R): List<R & Any> = (0 until length).mapNotNull { transform(item(it)) }

    fun Node.toTag(): Tag? = when (nodeName) {
        "g" -> Tag.Group(childNodes.mapNotNull { it.toTag() })
        "path" -> Tag.Path(attributes.getNamedItem("d").nodeValue)
        else -> null
    }
}