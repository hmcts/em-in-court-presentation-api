# In Court Presentation API

Currently when court attendees all need to look at a particular document they all have to sift through a pile 
of paper documents to find the correct one. These piles can become out of order and differences in printing may
mean that page 443 for one person is page 444 for another which can lead to confusion and waste time.

This project is looking to digitise this process using the existing 
[HMCTS Document Management Store](www.github.com/hmcts/document-management-store-api) and 
[Document Viewer](www.github.com/hmcts/em-viewer-web).  

A primary user can choose a document and initiate a new in court presentation session. This will generate a URL 
for the session that can be shared out to other users in the court room. Users can then log in and join the session.
Any changes to the primary user's view of the document such as page changes are then shared out to all users in the
session.

