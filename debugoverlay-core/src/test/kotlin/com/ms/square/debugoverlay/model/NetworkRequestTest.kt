package com.ms.square.debugoverlay.model

import com.google.common.truth.Truth.assertThat
import org.junit.Test

class NetworkRequestTest {

  @Test
  fun `toCurl formats GET request correctly`() {
    val request = NetworkRequest(
      method = "GET",
      url = "https://example.com/api/v1/users",
      statusCode = 200,
      durationMs = 150,
      responseSize = 1024,
      requestSize = 0,
      protocol = "h2",
      requestHeaders = mapOf(
        "Accept" to "application/json",
        "User-Agent" to "MyApp/1.0"
      )
    )

    val curl = request.toCurl(multiline = false)
    assertThat(
      curl
    ).isEqualTo(
      "curl -X GET -H \"Accept: application/json\" -H \"User-Agent: MyApp/1.0\" \"https://example.com/api/v1/users\""
    )

    val multilineCurl = request.toCurl(multiline = true)
    assertThat(multilineCurl).contains("curl -X GET")
    assertThat(multilineCurl).contains("-H \"Accept: application/json\"")
    assertThat(multilineCurl).contains("\"https://example.com/api/v1/users\"")
  }

  @Test
  fun `toCurl formats POST request with body and escapes quotes`() {
    val request = NetworkRequest(
      method = "POST",
      url = "https://example.com/graphql",
      statusCode = 200,
      durationMs = 250,
      responseSize = 2048,
      requestSize = 120,
      protocol = "h2",
      requestHeaders = mapOf(
        "Content-Type" to "application/json"
      ),
      requestBody = """{"name":"O'Reilly"}"""
    )

    val curl = request.toCurl(multiline = true)
    assertThat(curl).contains("curl -X POST")
    assertThat(curl).contains("-H \"Content-Type: application/json\"")
    // O'Reilly escaped as O'\''Reilly
    assertThat(curl).contains("-d '{\"name\":\"O'\\''Reilly\"}'")
    assertThat(curl).contains("\"https://example.com/graphql\"")
  }
}
