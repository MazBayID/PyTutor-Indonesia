package com.pytutor.baru

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

private val pythonTokens = Regex(
    """("(?:\\.|[^"\\])*"|'(?:\\.|[^'\\])*'|#.*|\b(?:False|None|True|and|as|assert|async|await|break|class|continue|def|del|elif|else|except|finally|for|from|global|if|import|in|is|lambda|nonlocal|not|or|pass|raise|return|try|while|with|yield)\b|\b(?:abs|all|any|bool|dict|enumerate|float|int|input|len|list|max|min|print|range|reversed|round|set|sorted|str|sum|tuple|type|zip)\b|\b\d+(?:\.\d+)?\b|\b[A-Za-z_][A-Za-z0-9_]*\b)"""
)

private val keywords = setOf(
    "False", "None", "True", "and", "as", "assert", "async", "await", "break",
    "class", "continue", "def", "del", "elif", "else", "except", "finally",
    "for", "from", "global", "if", "import", "in", "is", "lambda", "nonlocal",
    "not", "or", "pass", "raise", "return", "try", "while", "with", "yield"
)
private val builtins = setOf(
    "abs", "all", "any", "bool", "dict", "enumerate", "float", "int", "input",
    "len", "list", "max", "min", "print", "range", "reversed", "round", "set",
    "sorted", "str", "sum", "tuple", "type", "zip"
)

fun highlightPython(source: String): AnnotatedString = buildAnnotatedString {
    append(source)
    pythonTokens.findAll(source).forEach { match ->
        val token = match.value
        val next = source.substring(match.range.last + 1).dropWhile { it.isWhitespace() }.firstOrNull()
        val color = when {
            token.startsWith("#") -> Color(0xFF78909C)
            token.startsWith("\"") || token.startsWith("'") -> Color(0xFFC3E88D)
            token.firstOrNull()?.isDigit() == true -> Color(0xFFF78C6C)
            token in keywords -> Color(0xFFC792EA)
            token in builtins -> Color(0xFF82AAFF)
            next == '(' -> Color(0xFFFFCB6B)
            else -> Color(0xFFE8F4FF)
        }
        addStyle(SpanStyle(color = color), match.range.first, match.range.last + 1)
    }
}

val PythonSyntaxTransformation = VisualTransformation { value ->
    TransformedText(highlightPython(value.text), OffsetMapping.Identity)
}
