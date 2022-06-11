package soda.kotlin.web

@kotlinx.serialization.Serializable
data class WorkRequest(val classpath: String, val bootClass: String, val testCase: String)
