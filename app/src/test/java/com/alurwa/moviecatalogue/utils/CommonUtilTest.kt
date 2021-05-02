package com.alurwa.moviecatalogue.utils

import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.*
import org.junit.Test

class CommonUtilTest {
    @Test
    fun getLanguageName_inputEn_returnsEnglish() {
        val result = CommonUtil.getLanguageName("en")

        assertThat(result, `is`("English"))
    }

    @Test
    fun getLanguageName_inputInd_returnsIndonesian() {
        val result = CommonUtil.getLanguageName("in")

        assertThat(result, `is`("Indonesian"))
    }

    @Test
    fun getLanguageName_input3Digit_returnsNull() {
        val result = CommonUtil.getLanguageName("ina")

        assertThat(result, nullValue())
    }
}