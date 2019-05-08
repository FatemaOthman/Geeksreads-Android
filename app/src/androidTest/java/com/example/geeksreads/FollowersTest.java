package com.example.geeksreads;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class FollowersTest {

    //Error in instantiating an adapter.
/*
    @Rule
    public ActivityTestRule<Follow_Adapter> menuActivityTestRule =
            new ActivityTestRule<>(Follow_Adapter.class, true, true);
*/
    @Test
    public void Test_FollowersList() {


        String TestString = "{\"UserId\":\"1152003\",\"UserName\":\"Amr\",\"photo\":\"http:\\/\\/geeksreads.000webhostapp.com\\/Amr\\/MyPic.jpg\"},{\"UserId\":\"1142019\",\"UserName\":\"Fatema\",\"photo\":\"http:\\/\\/geeksreads.000webhostapp.com\\/Fatema\\/userPic.jpg\"}";
        //Test Server Response:
        assertEquals(TestString, Followers_Fragment.ForTestResponse);

    }
}
