package com.andresoller.architest.activities

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents.*
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.filters.LargeTest
import androidx.test.runner.AndroidJUnit4
import com.andresoller.architest.R
import com.andresoller.architest.activities.idlingresource.SimpleIdlingResource
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class PostsActivityTest {

    private var idlingResource: SimpleIdlingResource? = null
    lateinit var activityScenario: ActivityScenario<PostsActivity>

    @Before
    fun setUp() {
        init()
        activityScenario = ActivityScenario.launch(PostsActivity::class.java)
        activityScenario.onActivity { activity ->
            idlingResource = SimpleIdlingResource(activity.findViewById(R.id.recycler_posts))
            IdlingRegistry.getInstance().register(idlingResource!!)
        }
    }

    @Test
    fun clickRecyclerViewItem_shouldNavigateToPostDetailsActivity() {
        onView(withId(R.id.recycler_posts))
                .check(matches(isDisplayed()))

        onView(withText("Qui est esse"))
                .perform(click())

        intended(hasComponent(PostDetailActivity::class.java.name))
    }

    @After
    fun tearDown() {
        release()
        if (idlingResource != null) {
            IdlingRegistry.getInstance().unregister(idlingResource!!)
        }
    }
}
