package soda.kotlin.web.work

@kotlinx.serialization.Serializable
data class WorkRequest(val classpath: String, val bootClass: String, val testCase: String)
