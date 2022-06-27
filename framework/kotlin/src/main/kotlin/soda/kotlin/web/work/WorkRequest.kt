package soda.kotlin.web.work

@kotlinx.serialization.Serializable
data class WorkRequest(val key: String, val entryClass: String, val testCase: String)
