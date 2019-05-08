package com.example.geeksreads;

import android.support.test.espresso.assertion.ViewAssertions;
import android.support.test.rule.ActivityTestRule;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

public class OtherProfileTest {
    @Rule
    public ActivityTestRule<OtherProfileActivity> menuActivityTestRule =
            new ActivityTestRule<>(OtherProfileActivity.class, true, true);

    @Test
    public void Test_Other_Profile() {
        onView(withId(R.id.OtherUserName)).check(ViewAssertions.matches(withText("Amr Khaled")));
        assertEquals("http://geeksreads.000webhostapp.com/Amr/MyPic.jpg", OtherProfileActivity.aForTestUserPic);

    }

}
