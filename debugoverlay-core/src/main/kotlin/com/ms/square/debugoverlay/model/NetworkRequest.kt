package com.ms.square.debugoverlay.model

import kotlinx.serialization.Serializable
import java.util.UUID

/**
 * Data class for network request.
 *
 * This class supports kotlinx.serialization for export and custom integrations.
 */
@Serializable
public data class NetworkRequest(
  // Base NetworkRequest fields
  val id: String = UUID.randomUUID().toString(),
  val name: String? = null,
  val protocol: String?, // http/1.1, h2, quic..etc
  val method: String, // GET, POST, etc.
  val url: String, // https://test.com/api/v1/feed
  val statusCode: Int?, // 200, 404, etc.
  val durationMs: Long, // 245
  val responseSize: Long?, // bytes
  val requestSize: Long?, // bytes
  val timestampMs: Long = System.currentTimeMillis(),
  // Extended fields for detail screen
  val requestHeaders: Map<String, String> = emptyMap(),
  val responseHeaders: Map<String, String> = emptyMap(),
  val requestBody: String? = null,
  val responseBody: String? = null,
  val error: NetworkError? = null,
) {
  /**
   * Generates a copy-pasteable cURL command representation of this request.
   *
   * @param multiline whether to format with backslashes on multiple lines.
   */
  public fun toCurl(multiline: Boolean = true): String {
    val separator = if (multiline) " \\\n  " else " "
    val builder = StringBuilder("curl -X $method")
    for ((key, value) in requestHeaders) {
      builder.append("$separator-H \"$key: $value\"")
    }
    if (!requestBody.isNullOrEmpty()) {
      val escaped = requestBody.replace("'", "'\\''")
      builder.append("$separator-d '$escaped'")
    }
    builder.append("$separator\"$url\"")
    return builder.toString()
  }
}

/**
 * Error information for failed requests.
 *
 * This class supports kotlinx.serialization for export and custom integrations.
 */
@Serializable
public data class NetworkError(val title: String, val message: String, val stackTrace: String? = null)
