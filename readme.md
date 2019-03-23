Welcome Guys to our Android Repository of our Software Engineering Project. I wish this project to be light and easy on all of us.

Please take into consideration the below notes:

1] We are working on the "Android-Team" Branch not the "Master" nor the "team".

2] The used colors are:

    #baab98 , RGB(186, 171, 152).
    #857156 , RGB(133, 113, 86).
    #5b4931 , RGB(91, 73, 49).

3] We changed our logo a little bit, the new logo links are here in PNG format:

    Vertical Logo: https://github.com/WaelAshraf/Geeksreads-Android/blob/Android-Team/Logo.png .
    Horizontal Logo: https://github.com/WaelAshraf/Geeksreads-Android/blob/Android-Team/Logo2.png .

4] So far, for integrating github with Android Studio:

    1. Download Git setup from this link: https://git-scm.com/downloads "Download 2.21.0 for Windows"
    2. Run the setup, and press Next, Next, Next, "Use Git and optional Unix tools from the command prompt", Next, Next, Next, Next, Install.
    3. Open Android Studio
    4. Open File Menu, Select "Settings".
    5. From the side menu, select "Version Control" then "Git" NOT "GitHub"!!.
    6. In the "Path to Git executable" choose the location where you installed Git in step 1. (Usually it's installed in C:\Program Files\Git\bin\git.exe)
    7. Press Test to check if it works, it will show a message box containing the Git Version.
    8. From the Side Menu, select "Version Control" then "GitHub", Click Add or the "+" button on the top right.
    9. Write your github email address and password and press Login.
    10. Press Ok to close Settings Window.
    11. File Menu > New > From Version Control > Git > Url = https://github.com/WaelAshraf/Geeksreads-Android.git and Click Test
    12. It will Clone all the repository
    13. After it finishes, Open VCS Menu > Git > Branches > Remote Branches > Android-Team > Check Out As..
    14. In the small window that will appear, write "Android-Team" if it's not written and press Ok
    15. It will pull the branch and ask you for some Merge Acceptance , Accept every thing
    16. It will build and you may get an SDK Version error, Close the project and open it again (TEDATA)
    17. It wil open inssha'Allah.
    18. For Modifying anything in code, you have to commit all you changes with proper comment, (To Commit, VCS Menu > Commit)
    19. To Publish your modified code on github repo, Pull the latest version first then Merge your files with github files and then Push to Android-Team Branch
    20. To Pull: VCS Menu >> Git >> Pull {{PULL fetches the latest code on github then Merges the files}}
    21. To Push: VCS Menu >> Git >> Push

5] In order to facilitate future Integration between Android team and Back end team using a Public Host for APIs used, I created a real public host and domain for our project, so that we can upload and edit our Mimic Services on it and access them over internet from the Mobile Application. Here are some information regarding our Public Host:
    
    1. Public Domain Link: http://geeksreads.000webhostapp.com/
    2. Each one of us, will upload his files in a specific folder http://geeksreads.000webhostapp.com/<Folder Name>/ , Such as http://geeksreads.000webhostapp.com/Morsy/
    3. Host supports Php and MySQL.
    4. To Upload files, go to https://files.000webhost.com/
    5. Login Username: geeksreads
    6. Login Password: <Sent on Whatsapp Group>
    7. Every one should upload his files in his own folder only and don't edit any other files!
      
