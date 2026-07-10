package dev.gustavo.countries.data.local.database

import com.google.common.truth.Truth.assertThat
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

class StringListConverterTest {

    private lateinit var converter: StringListConverter
    private val json = Json

    @Before
    fun setUp() {
        converter = StringListConverter(json)
    }

    @Test
    fun `given list when fromList then returns json string`() {
        val list = listOf("a", "b", "c")
        val expected = "[\"a\",\"b\",\"c\"]"
        assertThat(converter.fromList(list)).isEqualTo(expected)
    }

    @Test
    fun `given json string when toList then returns list`() {
        val jsonString = "[\"a\",\"b\",\"c\"]"
        val expected = listOf("a", "b", "c")
        assertThat(converter.toList(jsonString)).isEqualTo(expected)
    }

    @Test
    fun `given blank string when toList then returns empty list`() {
        assertThat(converter.toList("")).isEmpty()
        assertThat(converter.toList("   ")).isEmpty()
    }

    @Test
    fun `given invalid json when toList then returns empty list`() {
        assertThat(converter.toList("invalid json")).isEmpty()
        assertThat(converter.toList("{ \"key\": \"value\" }")).isEmpty() // Not a list
    }
}
