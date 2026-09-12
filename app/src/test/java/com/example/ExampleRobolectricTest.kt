package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.AppLanguage
import com.example.data.model.Subject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SUMIT AI", appName)
  }

  @Test
  fun `verify language enum supports english hindi marathi`() {
    assertEquals(3, AppLanguage.entries.size)
    assertEquals(AppLanguage.ENGLISH, AppLanguage.fromCode("en"))
    assertEquals(AppLanguage.HINDI, AppLanguage.fromCode("hi"))
    assertEquals(AppLanguage.MARATHI, AppLanguage.fromCode("mr"))
  }

  @Test
  fun `verify subject localized titles`() {
    assertEquals("Math", Subject.MATH.localizedTitle(AppLanguage.ENGLISH))
    assertEquals("गणित", Subject.MATH.localizedTitle(AppLanguage.HINDI))
    assertEquals("गणित", Subject.MATH.localizedTitle(AppLanguage.MARATHI))

    assertEquals("Science", Subject.SCIENCE.localizedTitle(AppLanguage.ENGLISH))
    assertEquals("विज्ञान", Subject.SCIENCE.localizedTitle(AppLanguage.HINDI))
    assertEquals("विज्ञान", Subject.SCIENCE.localizedTitle(AppLanguage.MARATHI))

    assertEquals("Geography", Subject.GEOGRAPHY.localizedTitle(AppLanguage.ENGLISH))
    assertEquals("भूगोल", Subject.GEOGRAPHY.localizedTitle(AppLanguage.HINDI))
    assertEquals("भूगोल", Subject.GEOGRAPHY.localizedTitle(AppLanguage.MARATHI))

    assertEquals("Economics", Subject.ECONOMICS.localizedTitle(AppLanguage.ENGLISH))
    assertEquals("अर्थशास्त्र", Subject.ECONOMICS.localizedTitle(AppLanguage.HINDI))
    assertEquals("अर्थशास्त्र", Subject.ECONOMICS.localizedTitle(AppLanguage.MARATHI))

    assertEquals("Current Affairs", Subject.CURRENT_AFFAIRS.localizedTitle(AppLanguage.ENGLISH))
    assertEquals("समसामयिकी", Subject.CURRENT_AFFAIRS.localizedTitle(AppLanguage.HINDI))
    assertEquals("चालू घडामोडी", Subject.CURRENT_AFFAIRS.localizedTitle(AppLanguage.MARATHI))

    assertEquals("Marathi", Subject.MARATHI.localizedTitle(AppLanguage.ENGLISH))
    assertEquals("मराठी", Subject.MARATHI.localizedTitle(AppLanguage.HINDI))
    assertEquals("मराठी", Subject.MARATHI.localizedTitle(AppLanguage.MARATHI))

    assertEquals("Hindi", Subject.HINDI.localizedTitle(AppLanguage.ENGLISH))
    assertEquals("हिन्दी", Subject.HINDI.localizedTitle(AppLanguage.HINDI))
    assertEquals("हिंदी", Subject.HINDI.localizedTitle(AppLanguage.MARATHI))
  }
}

