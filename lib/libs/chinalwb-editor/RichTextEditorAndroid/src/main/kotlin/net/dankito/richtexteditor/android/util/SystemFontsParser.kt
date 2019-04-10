package net.dankito.richtexteditor.android.util

import org.slf4j.LoggerFactory
import java.io.File


class SystemFontsParser {

    companion object {
        private val log = LoggerFactory.getLogger(SystemFontsParser::class.java)
    }


    // TODO: for a real parsing of file see https://stackoverflow.com/a/29533686
    fun parseSystemFonts(): List<FontInfo> {
        val fontInfos = ArrayList<FontInfo>()

        fontInfos.addAll(parseFontInfosFromFontsXml()) // newer Androids

        fontInfos.addAll(parseFontInfosFromSystemFontsXml()) // older systems

        return fontInfos
    }


    private fun parseFontInfosFromFontsXml(): Collection<FontInfo> {
        val fontInfoMap = HashMap<String, FontInfo>()

        try {
            val fontsFile = File("/system/etc/fonts.xml")
            if(fontsFile.exists()) {
                val lines = fontsFile.readLines()
                for(i in 0..lines.size - 1) {
                    tryToParseFontsXmlLineToFontFamily(lines[i])?.let { fontFamily ->
                        val fontInfo = FontInfo(fontFamily)
                        fontInfoMap.put(fontFamily, fontInfo)

                        if(i < lines.size - 2) {
                            tryToParseFontsXmlFontName(lines[i + 1], fontInfo)
                        }
                    }
                }
            }
        } catch(e: Exception) { log.error("Could not parse font xml file /system/etc/fonts.xml", e) }

        return fontInfoMap.values
    }

    private fun tryToParseFontsXmlLineToFontFamily(line: String): String? {
        try {
            if(line.trim().startsWith("<family name=\"")) {
                var fontFamily = line.substring(line.indexOf("<family name=\"") + "<family name=\"".length)
                fontFamily = fontFamily.substring(0, fontFamily.indexOf('\"'))

                return fontFamily
            }
        } catch (e: Exception) {
            log.error("Could not parse line $line", e)
        }

        return null
    }

    private fun tryToParseFontsXmlFontName(line: String, fontInfo: FontInfo) {
        try {
            if(line.trim().startsWith("<font weight=\"")) {
                val startIndex = line.indexOf("\">")
                if(startIndex > 0) {
                    val fontNameLine = line.substring(line.indexOf("\">") + "\">".length)

                    getFontNameFromLine(fontNameLine, fontInfo)
                }
            }
        } catch(e: Exception) { log.error("Could not parse line $line to font name alias", e) }
    }


    private fun parseFontInfosFromSystemFontsXml(): Collection<FontInfo> {
        val fontInfos = ArrayList<FontInfo>()

        try {
            val fontsFile = File("/system/etc/system_fonts.xml")
            if(fontsFile.exists()) {
                val lines = fontsFile.readLines()
                for(i in 0..lines.size - 1) {
                    tryToParseSystemFontsXmlLineToFontInfo(lines, i)?.let { fontInfo ->
                        fontInfos.add(fontInfo)
                    }
                }
            }
        } catch(e: Exception) { log.error("Could not parse font xml file /system/etc/system_fonts.xml", e) }

        return fontInfos
    }

    private fun tryToParseSystemFontsXmlLineToFontInfo(lines: List<String>, index: Int): FontInfo? {
        try {
            val line = lines[index]
            if(line.trim() == "<nameset>") {
                val fontFamilyIndex = index + 1

                tryToParseSystemFontsXmlLineToFontFamily(lines, fontFamilyIndex)?.let { fontFamily ->
                    val fontInfo = FontInfo(fontFamily)

                    parseSystemFontsXmlFontName(fontInfo, lines, fontFamilyIndex)

                    return fontInfo
                }
            }
        } catch (e: Exception) {
            log.error("Could not parse line", e)
        }

        return null
    }

    private fun tryToParseSystemFontsXmlLineToFontFamily(lines: List<String>, index: Int): String? {
        try {
            val line = lines[index]

            if(line.trim().startsWith("<name>")) {
                var fontFamily = line.substring(line.indexOf("<name>") + "<name>".length)
                fontFamily = fontFamily.substring(0, fontFamily.indexOf("</name>"))

                return fontFamily
            }
        } catch(e: Exception) { log.error("Could not parse line for index $index", e) }

        return null
    }

    private fun parseSystemFontsXmlFontName(fontInfo: FontInfo, lines: List<String>, fontFamilyIndex: Int) {
        var nextIndex = fontFamilyIndex

        do {
            if(tryToParseSystemFontsXmlLineToFontName(lines, ++nextIndex, fontInfo)) {
                break
            }
        } while(nextIndex < lines.size - 1)
    }

    private fun tryToParseSystemFontsXmlLineToFontName(lines: List<String>, index: Int, fontInfo: FontInfo): Boolean {
        try {
            val line = lines[index]

            if(line.trim().startsWith("<fileset>")) {
                var fontNameLine = lines[index + 1]
                fontNameLine = fontNameLine.substring(fontNameLine.indexOf("<file>") + "<file>".length)
                getFontNameFromLine(fontNameLine, fontInfo)

                return true
            }
        } catch(e: Exception) { log.error("Could not parse line for index $index", e) }

        return false
    }


    private fun getFontNameFromLine(fontNameLine: String, fontInfo: FontInfo) {
        var endIndex = fontNameLine.indexOf('-')
        if(endIndex < 0) {
            endIndex = fontNameLine.indexOf('.')
        }

        if(endIndex > 0) {
            val fontName = fontNameLine.substring(0, endIndex)

            fontInfo.fontName = fontName
        }
    }

}