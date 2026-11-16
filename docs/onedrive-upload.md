Uploading the video and getting a link that actually opens

The video is D:\Administrator\Documents\A1-tysqnb-video\A1-tysqnb-video.mp4
(11.6 MB, 3:57).

Browser route
-------------
1. Go to https://www.office.com and sign in with your SCU account
   (the student address, not a personal one). If you land in Outlook, you are
   in the right tenant.
2. Open OneDrive from the app launcher (the grid, top left).
3. New > Folder, call it PROG2004-A1. Upload the mp4 into it. OneDrive may take
   a minute to process it; wait for the thumbnail, not just the name.
4. Right click the file > Share.
5. In the link settings, change "People in Southern Cross University" to
   "Anyone with the link", and set the permission to "Can view", not "Can edit".
   Apply.
6. Copy the link. It looks like
   https://1drv.ms/v/s!Am... or https://southerncrossuni-my.sharepoint.com/:v:/g/...
7. Open a new InPrivate window and paste the link. It has to play without you
   being signed in. If it asks you to sign in, the setting in step 5 did not
   take - redo it.

Then paste that link into docs/submission-comments.txt and into the MySCU
comment box.

Checks that catch the usual mistakes
------------------------------------
- The file plays in the InPrivate window, not just downloads.
- The link does not contain "guest access" or an expiry date.
- The video is in your SCU OneDrive, not a personal OneDrive, so the marker's
  tenant can open it.
- If you later rename or move the file, the link breaks. Share after the final
  version is in place.

Checking a link from the command line
-------------------------------------
    powershell -File check_link.ps1 "https://1drv.ms/v/..."

It prints the HTTP status and whether the response is a sign-in page. A 200
with no login redirect is what you want.

The link that did not work
--------------------------
    https://scuonline-my.sharepoint.com/:v:/r/personal/z_wang_105.../Documents/A1-tysqnb-video.mp4?d=w6dc...&csf=1&web=1&nav=...MyFilesLinkCopy&e=8xFxZl

Three things give it away:

- `/personal/<your account>/Documents/` is a path inside your own drive, not a
  sharing endpoint.
- `d=w6dc7d308a9b44a5d...` is the internal drive item id.
- `nav=...MyFilesLinkCopy` means it came from "Copy link" in the file details
  pane, which copies the address you would use while signed in.

Opening it in a browser with no session lands on login.microsoftonline.com, so
a marker who is not signed in to SCU gets a sign-in wall.

A share link looks different: the segment after the drive name is `/g/personal/...`
(g for guest) or a `1drv.ms` short link, and it has no `d=` item id. If the
"Anyone with the link" option is missing or greyed out, the tenant has turned
anonymous links off; fall back to "People at Southern Cross University" and
check the result with check_link.ps1 anyway.
