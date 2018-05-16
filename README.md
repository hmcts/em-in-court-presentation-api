# In Court Presentation API (Prototype)

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

### Status

Currently the In Court Presentation project is in concept. This is a prototype front end built to show stakeholders
the basic flow and get to grips with new technical challenges. 
As such it does not reflect the final project and does not include some fairly important like a database and load 
balancing.
The Github repository for the In Court Presentation front end prototype can be found at 
[https://github.com/hmcts/rpa-in-court-presentation-web](https://github.com/hmcts/rpa-in-court-presentation-web).

### Technologies

This project uses Spring Boot 2 and allows communication from the front end via REST and Websockets. We're using 
STOMP for pub/sub - so when the presenter changes document or page it publishes an event to the websocket that is then 
sent down to all subscribers for that hearing session.

### Considerations for productionisation

* Message Queue for a pub/sub broker between instances when load balanced.
* An actual database to hold hearing data and lists of current participants.
* Websocket connection seems a bit flaky on CNP. Need to work out why.
* Websockets Vs short polling?
