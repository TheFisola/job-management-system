# **JOB MANAGEMENT SYSTEM**

A simple job management system

### **Goal of the system**

The goal of this system is to handle the
execution of multiple types of Jobs. The actions performed by these Jobs are not important.

### **System Requirement**

- **Flexibility**

The types of possible actions performed by the Jobs are not known to the Job
Management System. In the future, new Jobs should be supported without re-developing
the Job Management System.

- **Reliability**

Each Job should either complete successfully or perform no action at all. (I.e. there should be
no side effects created by a Job that fails.)

- **Internal Consistency**

At any one time a Job has one of four states: QUEUED, RUNNING, SUCCESS, FAILED. Following
the execution of a Job, it should be left in an appropriate state.

- **Priority**

Each Job can be executed based on its priority relative to other Jobs.

- **Scheduling**

A Job can be executed immediately or according to a schedule.

### **THE DESIGNED SOLUTION**

- **Flexibility**

In order for the job management system to be job implementation agnostic. i.e. allow for new jobs to be added seamlessly. 
The system allows for jobs to be created with the intending job implementation url, request payload and headers

A sample job creation request for an email job:

`{
"requestUrl": "http://localhost:8091/sample-service/email",
"requestPayload": "{\"from\":\"randomemail@gmail.com\",\"to\":\"randomemail@yahoo.com\",\"body\":\"Random Email Body\",\"subject\":\"Random Email Subject!\"}",
"tag": "send-email",
"requestHeaders": {
"x-api-region": "USA"
},
"executionType": "SCHEDULED",
"scheduledExecutionDate": "2021-06-10 07:45:22"
}`

requestUrl - The Api that houses the actual job execution(In this case, send email implementation).

requestPayload - The request payload the job execution api expects(In this case, subject of the email, from, to, and the body of the email).

requestHeaders - Allows a client specify header information required by the execution api.

tag - A tag describing the purpose of the job.

executionType - This can be '_INSTANT_' or '_SCHEDULED_'. Allowing the client specify if they want the job executed immediately or at a scheduled time.

scheduledExecutionDate - Allows the client define the scheduled execution time if execution type is '_SCHEDULED_'

- **Reliability**

All job executions are made to have final states. A 200 Ok response determine the '_SUCCESS_' state, 
  other status code responses determine a '_FAILURE_' state.

- **Internal Consistency**
  
  QUEUED - A created job is at first assigned the '_QUEUED_' state. 
  RUNNING - A job at the start of execution is set to running.
  SUCCESS - A successful job takes the final state of '_SUCCESS_'.
  FAILED - A failed job takes the final state of '_FAILED_'.
  
- **Priority**

Priority can be defined by the client when creating a job and is used when ordering the execution of scheduled jobs.
The available priorities:

'_LOW_' | '_MEDIUM_' | '_HIGH_'.

- **Scheduling**
Execution types are provided for the client to define the type of execution:
  
'_INSTANT_' | '_SCHEDULED_'.

**Sample of a job in final state**

`{
"id": "a05fe68c-149e-408c-bab6-1243c101b48c",
"requestUrl": "http://localhost:8091/sample-service/email",
"requestPayload": "{\"from\":\"randomemail@gmail.com\",\"to\":\"randomemail@yahoo.com\",\"body\":\"Random Email Body\",\"subject\":\"Random Email Subject!\"}",
"tag": "send-email",
"state": "SUCCESS",
"priority": "HIGH",
"executionType": "SCHEDULED",
"scheduledExecutionDate": "2021-06-10 18:45:22",
"dateExecuted": "2021-06-10 18:46:12",
"dateCreated": "2021-06-10 12:15:34",
"dateUpdated": "2021-06-10 12:19:42"
}`

**Possible Improvements**

- The api currently supports only JSON job execution requests. An improvement will be to allow clients define different message types; XML, SOAP, Protobuf etc... 
- Jobs should be pushed to a message queue at the point of execution. A separate consumer service can act on the job request 

**Sample Job**

A sample job implementation is located on localhost:{YOUR_PORT}/sample-service/email

A sample job request to this would look like;

`{
"requestUrl": "http://localhost:8091/sample-service/email",
"requestPayload": "{\"from\":\"randomemail@gmail.com\",\"to\":\"randomemail@yahoo.com\",\"body\":\"Random Email Body\",\"subject\":\"Random Email Subject!\"}",
"tag": "send-email",
"requestHeaders": {
"x-api-region": "USA"
},
"executionType": "INSTANT",
}`

This would execute the job instantly.
