package com.example.geeksreads;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class EditProfileUnitTest {
    @Test
    public void EditProfileBirthDayChecker_isInvalid_InvalidDayInBirthDate() {
        assertEquals(false, EditProfileActivity.isThisDateValid("91/9/1995"));
    }
//    @Test
//    public void EditProfileBirthDayChecker_isInvalid_InvalidMonthInBirthDate() {
//        assertEquals(false, EditProfileActivity.isThisDateValid("1/950/1995"));
//    }
    @Test
    public void EditProfileBirthDayChecker_isInvalid_InvalidYearInBirthDate() {
        assertEquals(false, EditProfileActivity.isThisDateValid("1/9/199"));
    }
    @Test
    public void EditProfileBirthDayChecker_isInvalid_FutureYearInBirthDate() {
        assertEquals(false, EditProfileActivity.isThisDateValid("1/9/2020"));
    }
    @Test
    public void EditProfileBirthDayChecker_isInvalid_BirthDateLessThan5Years() {
        assertEquals(false, EditProfileActivity.isThisDateValid("1/9/2018"));
    }
    @Test
    public void EditProfileBirthDayChecker_isValid() {
        assertEquals(true, EditProfileActivity.isThisDateValid("1/9/1995"));
    }


    @Test
    public void EditProfileUsername_isInvalid_LessThan3Chars() {
        assertEquals(EditProfileActivity.editProfileValidationErrors.INVALID_USERNAME_LENGTH, EditProfileActivity.validateEditProfileData("Ma", "mahmoud_morsy@live", "1/9/1995"));
    }
    @Test
    public void EditProfileUsername_isInvalid_MoreThan50Chars() {
        assertEquals(EditProfileActivity.editProfileValidationErrors.INVALID_USERNAME_LENGTH, EditProfileActivity.validateEditProfileData("AbcdefghijklmnopqrstuvwxyzAbcdefghijklmnopqrstuvwxyz", "mahmoud_morsy@live", "1/9/1995"));
    }
    @Test
    public void EditProfileEmail_isInvalid_MissingDotCom() {
        assertEquals(EditProfileActivity.editProfileValidationErrors.INVALID_EMAIL, EditProfileActivity.validateEditProfileData("MahmoudMorsy", "mahmoud_morsy@live", "1/9/1995"));
    }
    @Test
    public void EditProfileEmail_isInvalid_MissingAt() {
        assertEquals(EditProfileActivity.editProfileValidationErrors.INVALID_EMAIL, EditProfileActivity.validateEditProfileData("MahmoudMorsy","mahmoud_morsy#live.com", "1/9/1995"));
    }
    @Test
    public void EditProfileEmail_isInvalid_MissingEmailName() {
        assertEquals(EditProfileActivity.editProfileValidationErrors.INVALID_EMAIL, EditProfileActivity.validateEditProfileData("MahmoudMorsy","@live.com", "1/9/1995"));
    }

    @Test
    public void EditProfileEmail_isInvalid_MissingHostName() {
        assertEquals(EditProfileActivity.editProfileValidationErrors.INVALID_EMAIL, EditProfileActivity.validateEditProfileData("MahmoudMorsy","mahmoud_morsy@.com", "1/9/1995"));
    }

    @Test
    public void EditProfileBirthDate_isInvalid_BirthDateLessThan5Years() {
        assertEquals(EditProfileActivity.editProfileValidationErrors.INVALID_BIRTH_DATE, EditProfileActivity.validateEditProfileData("MahmoudMorsy","mahmoud_morsy@live.com", "1/9/2016"));
    }
    @Test
    public void EditProfileData_areValid() {
        assertEquals(EditProfileActivity.editProfileValidationErrors.NO_ERRORS, EditProfileActivity.validateEditProfileData("MahmoudMorsy","mahmoud_morsy@live.com", "01/09/1995"));
    }
}