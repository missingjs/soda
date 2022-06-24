package soda.kotlin.web.http

import soda.kotlin.web.WebUtils

class ContentDisposition {

    var name: String = ""

    var filename: String? = null

    fun isFile(): Boolean {
        return filename != null
    }

    companion object {

        fun fromHeaderValue(value: String): ContentDisposition {
            val cname = WebUtils.findOne(value, "name=\"(.*?)\"")
                ?: throw IllegalArgumentException("no name found in Content-Disposition")
            return ContentDisposition().apply {
                name = cname
                filename = WebUtils.findOne(value, "filename=\"(.*?)\"")
            }
        }

        fun fromHeaderLine(headerLine: String): ContentDisposition {
            return fromHeaderValue(headerLine)
        }

    }

}